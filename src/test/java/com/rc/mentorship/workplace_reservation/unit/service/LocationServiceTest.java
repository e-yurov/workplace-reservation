package com.rc.mentorship.workplace_reservation.unit.service;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import com.rc.mentorship.workplace_reservation.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private LocationMapper locationMapper;
    private final UUID mockId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private LocationResponse locationResponse;

    @BeforeEach
    void beforeEach() {
        location = new Location();
        location.setId(mockId);

        locationResponse = new LocationResponse();
        locationResponse.setId(mockId);
    }

    @Test
    void findAllByCity_NoCityFilter_ReturningPageOf3() {
        PageRequest pageable = mock(PageRequest.class);
        Page<Location> locationPage = new PageImpl<>(List.of(new Location(), new Location(), new Location()));
        when(locationRepository.findAllByCityIfPresent(null, pageable)).thenReturn(locationPage);

        Page<LocationResponse> result = locationService.findAllByCity(pageable, null);

        assertThat(result).hasSize(3);
    }

    @Test
    void findById_HasLocationById_ReturningLocation() {
        when(locationRepository.findById(mockId)).thenReturn(Optional.of(location));
        when(locationMapper.toDto(location)).thenReturn(locationResponse);

        LocationResponse result = locationService.findById(mockId);

        assertThat(result).isNotNull().isEqualTo(locationResponse);
    }

    @Test
    void findById_NoLocationById_ThrowingNotFound() {
        when(locationRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.findById(mockId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_SimpleValues_ReturningCreatedLocation() {
        LocationCreateRequest request = new LocationCreateRequest();
        when(locationMapper.toEntity(request)).thenReturn(location);
        when(locationMapper.toDto(location)).thenReturn(locationResponse);

        LocationResponse result = locationService.create(request);

        assertThat(result).isNotNull().isEqualTo(locationResponse);
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void update_SimpleValues_ReturningUpdatedLocation() {
        LocationUpdateRequest request = new LocationUpdateRequest();
        request.setId(mockId);
        when(locationRepository.findById(mockId)).thenReturn(Optional.of(mock(Location.class)));
        when(locationMapper.toEntity(request)).thenReturn(location);
        when(locationMapper.toDto(location)).thenReturn(locationResponse);

        LocationResponse result = locationService.update(request);

        assertThat(result).isNotNull().isEqualTo(locationResponse);
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void update_NoLocationToUpdate_ThrowingNotFound() {
        LocationUpdateRequest request = new LocationUpdateRequest();
        request.setId(mockId);
        when(locationRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.update(request)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_SimpleValues_Deleted() {
        locationService.delete(mockId);

        verify(locationRepository, only()).deleteById(mockId);
    }
}
