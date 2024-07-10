package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponseDto;

import java.util.List;

public interface WorkplaceService {
    List<WorkplaceResponseDto> findAll();
    WorkplaceResponseDto findById(long id);
    WorkplaceResponseDto create(WorkplaceCreateRequestDto toCreate);
    WorkplaceResponseDto update(WorkplaceUpdateRequestDto toUpdate);
    void delete(long id);
    void deleteAll();
}
