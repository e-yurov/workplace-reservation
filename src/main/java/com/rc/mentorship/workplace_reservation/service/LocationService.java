package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    List<LocationResponse> findAll();

    LocationResponse findById(UUID id);

    LocationResponse create(LocationCreateRequest toCreate);

    LocationResponse update(LocationUpdateRequest toUpdate);

    void delete(UUID id);

    void deleteAll();
}
