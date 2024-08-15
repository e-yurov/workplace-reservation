package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.UUID;

public interface OfficeService {
    Page<OfficeResponse> findAllWithFilters(PageRequest pageRequest,
                                            Map<String, String> filters);

    OfficeResponse findById(UUID id);

    OfficeResponse create(OfficeCreateRequest toCreate);

    OfficeResponse update(OfficeUpdateRequest toUpdate);

    void delete(UUID id);
}
