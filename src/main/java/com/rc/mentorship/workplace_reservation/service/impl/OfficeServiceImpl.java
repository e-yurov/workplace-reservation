package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.OfficeMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {
    private final OfficeRepository officeRepository;
    private final LocationRepository locationRepository;
    private final OfficeMapper officeMapper;


    @Override
    @Transactional(readOnly = true)
    public List<OfficeResponse> findAll() {
        return officeRepository.findAll().stream().map(officeMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OfficeResponse findById(UUID id) {
        return officeMapper.toDto(officeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Office", id)
        ));
    }

    @Override
    @Transactional
    public OfficeResponse create(OfficeCreateRequest toCreate) {
        Office office = officeMapper.toEntity(toCreate);
        office.setLocation(locationRepository.findById(toCreate.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location",
                        toCreate.getLocationId())));
        officeRepository.save(office);
        return officeMapper.toDto(office);
    }

    @Override
    @Transactional
    public OfficeResponse update(OfficeUpdateRequest toUpdate) {
        officeRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Office", toUpdate.getId())
        );
        Office office = officeMapper.toEntity(toUpdate);
        office.setLocation(locationRepository.findById(toUpdate.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location",
                        toUpdate.getLocationId())));
        officeRepository.save(office);
        return officeMapper.toDto(office);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        officeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        officeRepository.deleteAll();
    }
}
