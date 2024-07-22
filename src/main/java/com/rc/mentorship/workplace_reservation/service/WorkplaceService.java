package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.UUID;

public interface WorkplaceService {
    Page<WorkplaceResponse> findAll(PageRequest pageRequest);

    Page<WorkplaceResponse> findAllWithFilters(PageRequest pageRequest,
                                            Map<String, Filter> fieldFilterMap);

    WorkplaceResponse findById(UUID id);

    WorkplaceResponse create(WorkplaceCreateRequest toCreate);

    WorkplaceResponse update(WorkplaceUpdateRequest toUpdate);

    void delete(UUID id);
}
