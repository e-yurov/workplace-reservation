package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponseDto;

import java.util.List;

public interface ReservationService {
    List<ReservationResponseDto> findAll();
    ReservationResponseDto findById(long id);
    ReservationResponseDto create(ReservationCreateRequestDto toCreate);
    ReservationResponseDto update(ReservationUpdateRequestDto toUpdate);
    void delete(long id);
    void deleteAll();
}
