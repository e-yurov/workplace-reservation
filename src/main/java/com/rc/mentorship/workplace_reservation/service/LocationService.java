package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponseDto;

import java.util.List;

public interface LocationService {
    List<LocationResponseDto> findAll();
    LocationResponseDto findById(long id);
    LocationResponseDto create(LocationCreateRequestDto toCreate);
    LocationResponseDto update(LocationUpdateRequestDto toUpdate);
    void delete(long id);
    void deleteAll();
}
