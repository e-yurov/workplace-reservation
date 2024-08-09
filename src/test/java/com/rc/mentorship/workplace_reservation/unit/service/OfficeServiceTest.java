package com.rc.mentorship.workplace_reservation.unit.service;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.OfficeMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.service.impl.OfficeServiceImpl;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterParamParser;
import com.rc.mentorship.workplace_reservation.util.filter.specifications.OfficeSpecs;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfficeServiceTest {
    @Mock
    private OfficeRepository officeRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private OfficeMapper officeMapper;
    private final UUID mockId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @InjectMocks
    private OfficeServiceImpl officeService;

    private Office office;
    private OfficeResponse officeResponse;

    @BeforeEach
    void beforeEach() {
        office = new Office();
        office.setId(mockId);

        officeResponse = new OfficeResponse();
        officeResponse.setId(mockId);
    }

    @Test
    void findAllWithFilters() {
        PageRequest pageRequest = mock(PageRequest.class);
        Page<Office> officePage = new PageImpl<>(List.of(new Office(), new Office(), new Office()));
        Map<String, String> filters = Collections.emptyMap();
        Map<String, Filter> fieldFilterMap = Collections.emptyMap();
        Specification<Office> allSpecs = mock(Specification.class);
        try (
                MockedStatic<FilterParamParser> filterParamParserMock = mockStatic(FilterParamParser.class);
                MockedStatic<OfficeSpecs> officeSpecsMock = mockStatic(OfficeSpecs.class)
        ) {
            filterParamParserMock.when(() -> FilterParamParser.parseAllParams(eq(filters), anySet())).thenReturn(fieldFilterMap);
            officeSpecsMock.when(() -> OfficeSpecs.build(fieldFilterMap)).thenReturn(allSpecs);
            when(officeRepository.findAll(allSpecs, pageRequest)).thenReturn(officePage);

            Page<OfficeResponse> result = officeService.findAllWithFilters(pageRequest, filters);

            assertThat(result).hasSize(3);
        }
    }

    @Test
    void findById() {
        when(officeRepository.findById(mockId)).thenReturn(Optional.of(office));
        when(officeMapper.toDto(office)).thenReturn(officeResponse);

        OfficeResponse result = officeService.findById(mockId);

        assertThat(result).isNotNull().isEqualTo(officeResponse);
    }

    @Test
    void findById_throwingNotFound() {
        when(officeRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> officeService.findById(mockId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create() {
        OfficeCreateRequest request = new OfficeCreateRequest();
        UUID locationMockId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        request.setLocationId(locationMockId);
        Location location = mock(Location.class);
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(locationMockId);
        officeResponse.setLocationResponse(locationResponse);
        when(officeMapper.toEntity(request)).thenReturn(office);
        when(locationRepository.findById(locationMockId)).thenReturn(Optional.of(location));
        when(officeMapper.toDto(office)).thenReturn(officeResponse);

        OfficeResponse response = officeService.create(request);

        assertThat(response).isNotNull().isEqualTo(officeResponse);
        verify(officeRepository, times(1)).save(office);
    }

    @Test
    void create_throwingNotFoundLocation() {
        OfficeCreateRequest request = new OfficeCreateRequest();
        UUID locationMockId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        request.setLocationId(locationMockId);
        when(officeMapper.toEntity(request)).thenReturn(office);
        when(locationRepository.findById(locationMockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> officeService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update() {
        OfficeUpdateRequest request = new OfficeUpdateRequest();
        request.setId(mockId);
        UUID locationMockId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        request.setLocationId(locationMockId);
        Location location = mock(Location.class);
        when(officeRepository.findById(mockId)).thenReturn(Optional.of(mock(Office.class)));
        when(officeMapper.toEntity(request)).thenReturn(office);
        when(locationRepository.findById(locationMockId)).thenReturn(Optional.of(location));
        when(officeMapper.toDto(office)).thenReturn(officeResponse);

        OfficeResponse result = officeService.update(request);

        assertThat(result).isNotNull().isEqualTo(officeResponse);
        verify(officeRepository, times(1)).save(office);
    }

    @Test
    void update_throwingNotFoundOffice() {
        OfficeUpdateRequest request = new OfficeUpdateRequest();
        request.setId(mockId);
        when(officeRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> officeService.update(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Office");
    }

    @Test
    void update_throwingNotFoundLocation() {
        OfficeUpdateRequest request = new OfficeUpdateRequest();
        request.setId(mockId);
        UUID locationMockId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        request.setLocationId(locationMockId);
        when(officeRepository.findById(mockId)).thenReturn(Optional.of(mock(Office.class)));
        when(officeMapper.toEntity(request)).thenReturn(office);
        when(locationRepository.findById(locationMockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> officeService.update(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Location");
    }

    @Test
    void delete() {
        officeService.delete(mockId);

        verify(officeRepository, only()).deleteById(mockId);
    }
}
