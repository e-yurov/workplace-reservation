package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import com.rc.mentorship.workplace_reservation.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<LocationResponse> findAllByCity(PageRequest pageRequest, String city) {
        return locationRepository.findAllByCityIfPresent(city, pageRequest)
                .map(locationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse findById(UUID id) {
        return locationMapper.toDto(locationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Location", id)
        ));
    }

    @Override
    @Transactional
    public LocationResponse create(LocationCreateRequest toCreate) {
        Location location = locationMapper.toEntity(toCreate);
        locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional
    public LocationResponse update(LocationUpdateRequest toUpdate) {
        locationRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new NotFoundException("Location", toUpdate.getId())
        );
        Location location = locationMapper.toEntity(toUpdate);
        locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        locationRepository.deleteById(id);
    }
}
