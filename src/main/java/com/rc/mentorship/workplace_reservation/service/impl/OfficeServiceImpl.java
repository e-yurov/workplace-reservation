package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToReadException;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToUpdateException;
import com.rc.mentorship.workplace_reservation.mapper.OfficeMapper;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepositoryInMemory;
import com.rc.mentorship.workplace_reservation.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {
    private final OfficeRepositoryInMemory officeRepository;
    private final OfficeMapper officeMapper;


    @Override
    public List<OfficeResponse> findAll() {
        return officeRepository.findAll().stream().map(officeMapper::toDto).toList();
    }

    @Override
    public OfficeResponse findById(UUID id) {
        return officeMapper.toDto(officeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundToReadException("Office")
        ));
    }

    @Override
    public OfficeResponse create(OfficeCreateRequest toCreate) {
        Office office = officeMapper.toEntity(toCreate);
        officeRepository.save(office);
        return officeMapper.toDto(office);
    }

    @Override
    public OfficeResponse update(OfficeUpdateRequest toUpdate) {
        officeRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundToUpdateException("Office")
        );
        Office office = officeMapper.toEntity(toUpdate);
        officeRepository.update(office);
        return officeMapper.toDto(office);
    }

    @Override
    public void delete(UUID id) {
        officeRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        officeRepository.deleteAll();
    }
}
