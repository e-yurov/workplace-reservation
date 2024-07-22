package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.UUID;

public interface ReservationService {
    Page<ReservationResponse> findAll(PageRequest pageRequest);

    Page<ReservationResponse> findAllWithFilters(PageRequest pageRequest,
                                                 Map<String, Filter> fieldFilterMap);

    ReservationResponse findById(UUID id);

    ReservationResponse create(ReservationCreateRequest toCreate);

    ReservationResponse update(ReservationUpdateRequest toUpdate);

    void delete(UUID id);

    void deleteAll();
}
