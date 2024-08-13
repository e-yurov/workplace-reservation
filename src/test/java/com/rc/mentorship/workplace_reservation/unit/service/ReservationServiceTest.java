package com.rc.mentorship.workplace_reservation.unit.service;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.entity.ReservationDateTime;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.BadReservationRequestException;
import com.rc.mentorship.workplace_reservation.exception.BadReservationTimeException;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.exception.WorkplaceNotAvailableException;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
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
    private ReservationMapper reservationMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkplaceRepository workplaceRepository;

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
    private User user;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void beforeEach() {
        reservation = new Reservation();
        reservation.setId(mockId);
        reservation.setDateTime(dateTime);

        workplace = new Workplace();
        workplace.setId(workplaceMockId);

        user = new User();
        user.setId(userMockId);

        reservationResponse = new ReservationResponse();
        reservationResponse.setId(mockId);
    }

    @Test
    void findAllWithFilters() {
        PageRequest pageRequest = mock(PageRequest.class);
        Page<Reservation> officePage = new PageImpl<>(List.of(new Reservation(), new Reservation(), new Reservation()));
        Map<String, String> filters = Collections.emptyMap();
        Map<String, Filter> fieldFilterMap = Collections.emptyMap();
        Specification<Reservation> allSpecs = mock(Specification.class);
        try (
                MockedStatic<FilterParamParser> filterParamParserMock = mockStatic(FilterParamParser.class);
                MockedStatic<ReservationSpecs> reservationSpecsMock = mockStatic(ReservationSpecs.class)
        ) {
            filterParamParserMock.when(() -> FilterParamParser.parseAllParams(eq(filters), anySet())).thenReturn(fieldFilterMap);
            reservationSpecsMock.when(() -> ReservationSpecs.build(fieldFilterMap)).thenReturn(allSpecs);
            when(reservationRepository.findAll(allSpecs, pageRequest)).thenReturn(officePage);

            Page<ReservationResponse> result = reservationService.findAllWithFilters(pageRequest, filters);

            assertThat(result).hasSize(3);
        }
    }

    @Test
    void findById(){
        when(reservationRepository.findById(mockId)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDto(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.findById(mockId);

        assertThat(result).isNotNull().isEqualTo(reservationResponse);
    }

    @Test
    void findById_throwingNotFound() {
        when(reservationRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.findById(mockId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        request.setUserId(userMockId);
        workplace.isAvailable(true);
        reservation.setId(null);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));
        when(reservationRepository.checkReserved(workplaceMockId, null, dateTime.getStart(), dateTime.getEnd()))
                .thenReturn(Collections.emptyList());
        when(userRepository.findById(userMockId)).thenReturn(Optional.of(user));
        when(reservationMapper.toDto(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.create(request);

        assertThat(result).isNotNull().isEqualTo(reservationResponse);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void create_throwingNotFoundWorkplace() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Workplace");
    }

    @Test
    void create_throwingNotFoundUser() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        request.setUserId(userMockId);
        workplace.isAvailable(true);
        reservation.setId(null);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));
        when(reservationRepository.checkReserved(workplaceMockId, null, dateTime.getStart(), dateTime.getEnd()))
                .thenReturn(Collections.emptyList());
        when(userRepository.findById(userMockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("User");
    }

    @Test
    void create_throwingBadReservationTime() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        dateTime.setEnd(dateTime.getStart().minusHours(1));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(BadReservationTimeException.class);
    }

    @Test
    void create_throwingWorkplaceNotAvailable() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setWorkplaceId(workplaceMockId);
        workplace.isAvailable(false);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(workplaceRepository.findById(workplaceMockId)).thenReturn(Optional.of(workplace));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(WorkplaceNotAvailableException.class);
    }

    @Test
    void create_throwingBadReservationRequest() {
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
    void update() {
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
        when(userRepository.findById(userMockId)).thenReturn(Optional.of(user));
        when(reservationMapper.toDto(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.update(request);

        assertThat(result).isNotNull().isEqualTo(reservationResponse);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void update_throwingNotFoundReservation() {
        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setId(mockId);
        when(reservationRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.update(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Reservation");
    }

    @Test
    void delete() {
        reservationService.delete(mockId);

        verify(reservationRepository, only()).deleteById(mockId);
    }
}
