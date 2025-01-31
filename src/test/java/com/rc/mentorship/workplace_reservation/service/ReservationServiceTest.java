package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.entity.ReservationDateTime;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.BadReservationRequestException;
import com.rc.mentorship.workplace_reservation.exception.BadReservationTimeException;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.exception.WorkplaceNotAvailableException;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.impl.ReservationServiceImpl;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterParamParser;
import com.rc.mentorship.workplace_reservation.util.filter.specifications.ReservationSpecs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private WorkplaceRepository workplaceRepository;
    @Mock
    private ReservationMapper reservationMapper;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private final UUID mockId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final UUID workplaceMockId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final UUID userMockId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private final ReservationDateTime dateTime = new ReservationDateTime(
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(4)
    );

    private Reservation reservation;
    private Workplace workplace;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void beforeEach() {
        reservation = new Reservation();
        reservation.setId(mockId);
        reservation.setDateTime(dateTime);
        reservation.setUserId(userMockId);

        workplace = new Workplace();
        workplace.setId(workplaceMockId);

        reservationResponse = new ReservationResponse();
        reservationResponse.setId(mockId);
    }

    @Test
    void findAllWithFilters_NoFilters_ReturningPageOf3() {
        PageRequest pageRequest = mock(PageRequest.class);
        Page<Reservation> reservationPage = new PageImpl<>(List.of(new Reservation(), new Reservation(), new Reservation()));
        Map<String, String> filters = Collections.emptyMap();
        Map<String, Filter> fieldFilterMap = Collections.emptyMap();
        Specification<Reservation> allSpecs = mock(Specification.class);
        try (
                MockedStatic<FilterParamParser> filterParamParserMock = mockStatic(FilterParamParser.class);
                MockedStatic<ReservationSpecs> reservationSpecsMock = mockStatic(ReservationSpecs.class)
        ) {
            filterParamParserMock.when(() -> FilterParamParser.parseAllParams(eq(filters), anySet())).thenReturn(fieldFilterMap);
            reservationSpecsMock.when(() -> ReservationSpecs.build(fieldFilterMap)).thenReturn(allSpecs);
            when(reservationRepository.findAll(allSpecs, pageRequest)).thenReturn(reservationPage);
            when(reservationMapper.toDto(any(Reservation.class))).thenReturn(reservationResponse);

            Page<ReservationResponse> result = reservationService.findAllWithFilters(pageRequest, filters);

            assertThat(result).hasSize(3);
        }
    }

    @Test
    void findById_HasReservationById_ReturningReservation(){
        when(reservationRepository.findById(mockId)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDto(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.findById(mockId);

        assertThat(result).isNotNull().isEqualTo(reservationResponse);
    }

    @Test
    void findById_NoReservationById_ThrowingNotFound() {
        when(reservationRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.findById(mockId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_SimpleValues_ReturningCreatedReservation() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        request.setUserId(userMockId);
        workplace.isAvailable(true);
        reservation.setId(null);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));
        when(reservationRepository.checkReserved(workplaceMockId, null, dateTime.getStart(), dateTime.getEnd()))
                .thenReturn(Collections.emptyList());
        when(userService.getUserById(userMockId)).thenReturn(mock(UserResponse.class));
        when(reservationMapper.toDto(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.create(request);

        assertThat(result).isNotNull().isEqualTo(reservationResponse);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void create_NoWorkplace_ThrowingNotFound() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Workplace");
    }

    @Test
    void create_NoUser_ThrowingNotFound() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        request.setUserId(userMockId);
        workplace.isAvailable(true);
        reservation.setId(null);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));
        when(reservationRepository.checkReserved(workplaceMockId, null, dateTime.getStart(), dateTime.getEnd()))
                .thenReturn(Collections.emptyList());
        when(userService.getUserById(userMockId)).thenThrow(new NotFoundException("User", mockId));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("User");
    }

    @Test
    void create_TimeOfStartAfterTimeOfEnd_ThrowingBadReservationTime() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        dateTime.setEnd(dateTime.getStart().minusHours(1));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(BadReservationTimeException.class);
    }

    @Test
    void create_WorkplaceNotAvailableForReservation_ThrowingWorkplaceNotAvailable() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        workplace.isAvailable(false);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(WorkplaceNotAvailableException.class);
    }

    @Test
    void create_AlreadyReserved_ThrowingBadReservationRequest() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        workplace.isAvailable(true);
        reservation.setId(null);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));
        when(reservationRepository.checkReserved(workplaceMockId, null, dateTime.getStart(), dateTime.getEnd()))
                .thenReturn(List.of(new Reservation()));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(BadReservationRequestException.class);
    }

    @Test
    void update_SimpleValues_ReturningUpdatedReservation() {
        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setId(mockId);
        request.setWorkplaceId(workplaceMockId);
        request.setUserId(userMockId);
        workplace.isAvailable(true);
        when(reservationRepository.findById(mockId)).thenReturn(Optional.of(mock(Reservation.class)));
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));
        when(reservationRepository.checkReserved(workplaceMockId, mockId, dateTime.getStart(), dateTime.getEnd()))
                .thenReturn(Collections.emptyList());
        when(userService.getUserById(userMockId)).thenReturn(mock(UserResponse.class));
        when(reservationMapper.toDto(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.update(request);

        assertThat(result).isNotNull().isEqualTo(reservationResponse);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void update_NoReservationToUpdate_ThrowingNotFound() {
        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setId(mockId);
        when(reservationRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.update(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Reservation");
    }

    @Test
    void delete_SimpleValues_Deleted() {
        reservationService.delete(mockId);

        verify(reservationRepository, only()).deleteById(mockId);
    }
}
