package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToReadException;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToUpdateException;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import com.rc.mentorship.workplace_reservation.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public List<LocationResponse> findAll() {
        return locationRepository.findAll().stream().map(locationMapper::toDto).toList();
    }

    @Override
    public LocationResponse findById(UUID id) {
        return locationMapper.toDto(locationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundToReadException("Location")
        ));
    }

    @Override
    public LocationResponse create(LocationCreateRequest toCreate) {
        Location location = locationMapper.toEntity(toCreate);
        locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public LocationResponse update(LocationUpdateRequest toUpdate) {
        locationRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundToUpdateException("Location")
        );
        Location location = locationMapper.toEntity(toUpdate);
        locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public void delete(UUID id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        locationRepository.deleteAll();
    }
}
