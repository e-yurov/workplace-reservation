package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponseDto;

import java.util.List;

public interface OfficeService {
    List<OfficeResponseDto> findAll();
    OfficeResponseDto findById(long id);
    OfficeResponseDto create(OfficeCreateRequestDto toCreate);
    OfficeResponseDto update(OfficeUpdateRequestDto toUpdate);
    void delete(long id);
    void deleteAll();
}
