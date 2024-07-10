package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;

import java.util.List;
import java.util.UUID;

public interface OfficeService {
    List<OfficeResponse> findAll();

    OfficeResponse findById(UUID id);

    OfficeResponse create(OfficeCreateRequest toCreate);

    OfficeResponse update(OfficeUpdateRequest toUpdate);

    void delete(UUID id);

    void deleteAll();
}
