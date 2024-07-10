package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    List<ReservationResponse> findAll();

    ReservationResponse findById(UUID id);

    ReservationResponse create(ReservationCreateRequest toCreate);

    ReservationResponse update(ReservationUpdateRequest toUpdate);

    void delete(UUID id);

    void deleteAll();
}
