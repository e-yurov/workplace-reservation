package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OfficeMapper.class)
public interface WorkplaceMapper {
    @Mapping(source = "computerPresent", target = "isComputerPresent")
    @Mapping(source = "available", target = "isAvailable")
    Workplace toEntity(WorkplaceCreateRequest requestDto);

    @Mapping(source = "computerPresent", target = "isComputerPresent")
    @Mapping(source = "available", target = "isAvailable")
    Workplace toEntity(WorkplaceUpdateRequest requestDto);

    @Mapping(source = "office", target = "officeResponse")
    @Mapping(source = "computerPresent", target = "computerPresent")
    @Mapping(source = "available", target = "available")
    WorkplaceResponse toDto(Workplace entity);
}
