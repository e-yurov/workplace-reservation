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
    @Mapping(source = "startDateTime", target = "dateTime.start")
    @Mapping(source = "endDateTime", target = "dateTime.end")
    Reservation toEntity(ReservationCreateRequest requestDto);

    @Mapping(source = "startDateTime", target = "dateTime.start")
    @Mapping(source = "endDateTime", target = "dateTime.end")
    Reservation toEntity(ReservationUpdateRequest requestDto);

    @Mapping(source = "dateTime.start", target = "startDateTime")
    @Mapping(source = "dateTime.end", target = "endDateTime")
    @Mapping(source = "user", target = "userResponse")
    @Mapping(source = "workplace", target = "workplaceResponse")
    ReservationResponse toDto(Reservation entity);
}
