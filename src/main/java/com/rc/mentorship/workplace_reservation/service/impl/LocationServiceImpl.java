package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponseDto;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepositoryInMemory;
import com.rc.mentorship.workplace_reservation.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepositoryInMemory locationRepository;
    @Autowired
    private LocationMapper locationMapper;

    @Override
    public List<LocationResponseDto> findAll() {
        return locationRepository.findAll().stream().map(locationMapper::toDto).toList();
    }

    @Override
    public LocationResponseDto findById(long id) {
        //TODO: create custom exception
        return locationMapper.toDto(locationRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public LocationResponseDto create(LocationCreateRequestDto toCreate) {
        Location location = locationMapper.toEntity(toCreate);
        locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public LocationResponseDto update(LocationUpdateRequestDto toUpdate) {
        //TODO: create custom exception
        if (locationRepository.findById(toUpdate.getId()).isEmpty()) {
            throw new RuntimeException();
        }
        Location location = locationMapper.toEntity(toUpdate);
        locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public void delete(long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        locationRepository.deleteAll();
    }
}
