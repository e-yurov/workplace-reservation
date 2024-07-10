package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponseDto;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    List<LocationResponseDto> findAll();

    LocationResponseDto findById(UUID id);

    LocationResponseDto create(LocationCreateRequestDto toCreate);

    LocationResponseDto update(LocationUpdateRequestDto toUpdate);

    void delete(UUID id);

    void deleteAll();
}
