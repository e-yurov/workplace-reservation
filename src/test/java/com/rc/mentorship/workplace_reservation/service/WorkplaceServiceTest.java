package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.WorkplaceMapper;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.impl.WorkplaceServiceImpl;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterParamParser;
import com.rc.mentorship.workplace_reservation.util.filter.specifications.WorkplaceSpecs;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkplaceServiceTest {
    @Mock
    private WorkplaceRepository workplaceRepository;
    @Mock
    private OfficeRepository officeRepository;
    @Mock
    private WorkplaceMapper workplaceMapper;
    private final UUID mockId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final UUID officeMockId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @InjectMocks
    private WorkplaceServiceImpl workplaceService;

    private Workplace workplace;
    private WorkplaceResponse workplaceResponse;

    @BeforeEach
    void beforeEach() {
        workplace = new Workplace();
        workplace.setId(mockId);

        workplaceResponse = new WorkplaceResponse();
        workplaceResponse.setId(mockId);
    }

    @Test
    void findAllWithFilters_NoFilters_ReturningPageOf3() {
        PageRequest pageRequest = mock(PageRequest.class);
        Page<Workplace> workplacePage = new PageImpl<>(List.of(new Workplace(), new Workplace(), new Workplace()));
        Map<String, String> filters = Collections.emptyMap();
        Map<String, Filter> fieldFilterMap = Collections.emptyMap();
        Specification<Workplace> allSpecs = mock(Specification.class);
        try (
                MockedStatic<FilterParamParser> filterParamParserMock = mockStatic(FilterParamParser.class);
                MockedStatic<WorkplaceSpecs> reservationSpecsMock = mockStatic(WorkplaceSpecs.class)
        ) {
            filterParamParserMock.when(() -> FilterParamParser.parseAllParams(eq(filters), anySet())).thenReturn(fieldFilterMap);
            reservationSpecsMock.when(() -> WorkplaceSpecs.build(fieldFilterMap)).thenReturn(allSpecs);
            when(workplaceRepository.findAll(allSpecs, pageRequest)).thenReturn(workplacePage);

            Page<WorkplaceResponse> result = workplaceService.findAllWithFilters(pageRequest, filters);

            assertThat(result).hasSize(3);
        }
    }

    @Test
    void findById_HasWorkplaceById_ReturningWorkplace() {
        when(workplaceRepository.findById(mockId)).thenReturn(Optional.of(workplace));
        when(workplaceMapper.toDto(workplace)).thenReturn(workplaceResponse);

        WorkplaceResponse result = workplaceService.findById(mockId);

        assertThat(result).isNotNull().isEqualTo(workplaceResponse);
    }

    @Test
    void findById_NoWorkplaceById_ThrowingNotFound() {
        when(workplaceRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workplaceService.findById(mockId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_SimpleValues_ReturningCreatedWorkplace() {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest();
        request.setOfficeId(officeMockId);
        Office office = new Office();
        office.setId(officeMockId);
        when(workplaceMapper.toEntity(request)).thenReturn(workplace);
        when(officeRepository.findById(officeMockId)).thenReturn(Optional.of(office));
        when(workplaceMapper.toDto(workplace)).thenReturn(workplaceResponse);

        WorkplaceResponse result = workplaceService.create(request);

        assertThat(result).isNotNull().isEqualTo(workplaceResponse);
        verify(workplaceRepository, times(1)).save(workplace);
    }

    @Test
    void create_NoOffice_ThrowingNotFound() {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest();
        request.setOfficeId(officeMockId);
        when(workplaceMapper.toEntity(request)).thenReturn(workplace);
        when(officeRepository.findById(officeMockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workplaceService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Office");
    }

    @Test
    void update_SimpleValues_ReturningUpdatedWorkplace() {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest();
        request.setId(mockId);
        request.setOfficeId(officeMockId);
        Office office = new Office();
        office.setId(officeMockId);
        when(workplaceRepository.findById(mockId)).thenReturn(Optional.of(mock(Workplace.class)));
        when(workplaceMapper.toEntity(request)).thenReturn(workplace);
        when(officeRepository.findById(officeMockId)).thenReturn(Optional.of(office));
        when(workplaceMapper.toDto(workplace)).thenReturn(workplaceResponse);

        WorkplaceResponse result = workplaceService.update(request);

        assertThat(result).isNotNull().isEqualTo(workplaceResponse);
        verify(workplaceRepository, times(1)).save(workplace);
    }

    @Test
    void update_NoWorkplaceToUpdate_ThrowingNotFound() {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest();
        request.setId(mockId);
        when(workplaceRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workplaceService.update(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Workplace");
    }

    @Test
    void update_NoOffice_ThrowingNotFound() {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest();
        request.setId(mockId);
        request.setOfficeId(officeMockId);
        when(workplaceRepository.findById(mockId)).thenReturn(Optional.of(mock(Workplace.class)));
        when(workplaceMapper.toEntity(request)).thenReturn(workplace);
        when(officeRepository.findById(officeMockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workplaceService.update(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Office");
    }

    @Test
    void delete_SimpleValues_Deleted() {
        workplaceService.delete(mockId);

        verify(workplaceRepository, only()).deleteById(mockId);
    }
}
