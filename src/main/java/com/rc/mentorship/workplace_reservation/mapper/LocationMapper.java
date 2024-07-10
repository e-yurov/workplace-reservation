package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponseDto;
import com.rc.mentorship.workplace_reservation.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toEntity(LocationCreateRequestDto requestDto);

    Location toEntity(LocationUpdateRequestDto requestDto);

    LocationResponseDto toDto(Location entity);
}
