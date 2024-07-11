package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    Reservation toEntity(ReservationCreateRequest requestDto);

    Reservation toEntity(ReservationUpdateRequest requestDto);

    ReservationResponse toDto(Reservation entity);
}
