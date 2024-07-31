package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.OfficeMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.service.OfficeService;
import com.rc.mentorship.workplace_reservation.util.filter.FilterParamParser;
import com.rc.mentorship.workplace_reservation.util.filter.specifications.OfficeSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {
    private final OfficeRepository officeRepository;
    private final LocationRepository locationRepository;
    private final OfficeMapper officeMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OfficeResponse> findAll(PageRequest pageRequest) {
        return officeRepository.findAll(pageRequest).map(officeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfficeResponse> findAllWithFilters(PageRequest pageRequest,
                                                   Map<String, String> filters) {
        Specification<Office> allSpecs = OfficeSpecs.build(
                FilterParamParser.parseAllParams(
                        filters, Set.of("pageNumber", "pageSize"))
        );
        return officeRepository.findAll(allSpecs, pageRequest).map(officeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OfficeResponse findById(UUID id) {
        return officeMapper.toDto(officeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Office", id)
        ));
    }

    @Override
    @Transactional
    public OfficeResponse create(OfficeCreateRequest toCreate) {
        Office office = officeMapper.toEntity(toCreate);
        office.setLocation(locationRepository.findById(toCreate.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location",
                        toCreate.getLocationId())));
        officeRepository.save(office);
        return officeMapper.toDto(office);
    }

    @Override
    @Transactional
    public OfficeResponse update(OfficeUpdateRequest toUpdate) {
        officeRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new NotFoundException("Office", toUpdate.getId())
        );
        Office office = officeMapper.toEntity(toUpdate);
        office.setLocation(locationRepository.findById(toUpdate.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location",
                        toUpdate.getLocationId())));
        officeRepository.save(office);
        return officeMapper.toDto(office);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        officeRepository.deleteById(id);
    }
}
