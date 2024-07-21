package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface LocationService {
    Page<LocationResponse> findAll(PageRequest pageRequest);

    Page<LocationResponse> findAllByCity(PageRequest pageRequest, String city);

    LocationResponse findById(UUID id);

    LocationResponse create(LocationCreateRequest toCreate);

    LocationResponse update(LocationUpdateRequest toUpdate);

    void delete(UUID id);

    void deleteAll();
}
