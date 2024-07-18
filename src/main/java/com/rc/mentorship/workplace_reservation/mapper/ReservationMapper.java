package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, WorkplaceMapper.class})
public interface ReservationMapper {
    Reservation toEntity(ReservationCreateRequest requestDto);

    Reservation toEntity(ReservationUpdateRequest requestDto);

    @Mapping(source = "user", target = "userResponse")
    @Mapping(source = "workplace", target = "workplaceResponse")
    ReservationResponse toDto(Reservation entity);
}
