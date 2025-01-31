package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.UUID;

public interface WorkplaceService {
    Page<WorkplaceResponse> findAllWithFilters(PageRequest pageRequest,
                                            Map<String, String> fieldFilterMap);

    WorkplaceResponse findById(UUID id);

    WorkplaceResponse create(WorkplaceCreateRequest toCreate);

    WorkplaceResponse update(WorkplaceUpdateRequest toUpdate);

    void delete(UUID id);
}
