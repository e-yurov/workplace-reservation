package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.Office;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface OfficeMapper {
    @Mapping(source = "startTime", target = "workTime.startTime")
    @Mapping(source = "endTime", target = "workTime.endTime")
    Office toEntity(OfficeCreateRequest requestDto);

    @Mapping(source = "startTime", target = "workTime.startTime")
    @Mapping(source = "endTime", target = "workTime.endTime")
    Office toEntity(OfficeUpdateRequest requestDto);

    @Mapping(source = "location", target = "locationResponse")
    @Mapping(source = "workTime.startTime", target = "startTime")
    @Mapping(source = "workTime.endTime", target = "endTime")
    OfficeResponse toDto(Office entity);
}
