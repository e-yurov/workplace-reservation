package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import com.rc.mentorship.workplace_reservation.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

public class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private LocationMapper locationMapper;
    private final UUID mockId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private LocationService locationService;
    private Location location;
    private LocationResponse locationResponse;

    @BeforeEach
    void beforeEach() {
        locationRepository = Mockito.mock(LocationRepository.class);
        locationMapper = Mockito.mock(LocationMapper.class);

        location = new Location();
        location.setId(mockId);

        locationResponse = new LocationResponse();
        locationResponse.setId(mockId);

        locationService = new LocationServiceImpl(locationRepository, locationMapper);
    }

    @Test
    void findById() {
        Mockito.when(locationRepository.findById(mockId)).thenReturn(Optional.of(location));
        Mockito.when(locationMapper.toDto(location)).thenReturn(locationResponse);

        LocationResponse actualResponse = locationService.findById(mockId);
        Assertions.assertEquals(locationResponse, actualResponse);
    }
}
