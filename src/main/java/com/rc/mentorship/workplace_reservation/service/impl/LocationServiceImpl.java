package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepositoryInMemory;
import com.rc.mentorship.workplace_reservation.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepositoryInMemory locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public List<LocationResponse> findAll() {
        return locationRepository.findAll().stream().map(locationMapper::toDto).toList();
    }

    @Override
    public LocationResponse findById(UUID id) {
        //TODO: create custom exception
        return locationMapper.toDto(locationRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public LocationResponse create(LocationCreateRequest toCreate) {
        Location location = locationMapper.toEntity(toCreate);
        locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public LocationResponse update(LocationUpdateRequest toUpdate) {
        //TODO: create custom exception
        if (locationRepository.findById(toUpdate.getId()).isEmpty()) {
            throw new RuntimeException();
        }
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
