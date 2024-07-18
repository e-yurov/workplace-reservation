package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.Office;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface OfficeMapper {
    Office toEntity(OfficeCreateRequest requestDto);

    Office toEntity(OfficeUpdateRequest requestDto);

    @Mapping(source = "location", target = "locationResponse")
    OfficeResponse toDto(Office entity);
}
