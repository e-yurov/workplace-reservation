package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;

import java.util.List;
import java.util.UUID;

public interface WorkplaceService {
    List<WorkplaceResponse> findAll();

    WorkplaceResponse findById(UUID id);

    WorkplaceResponse create(WorkplaceCreateRequest toCreate);

    WorkplaceResponse update(WorkplaceUpdateRequest toUpdate);

    void delete(UUID id);

    void deleteAll();
}
