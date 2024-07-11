package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkplaceMapper {
    Workplace toEntity(WorkplaceCreateRequest requestDto);

    Workplace toEntity(WorkplaceUpdateRequest requestDto);

    WorkplaceResponse toDto(Workplace entity);
}
