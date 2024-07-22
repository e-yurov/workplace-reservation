package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.WorkplaceMapper;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.WorkplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkplaceServiceImpl implements WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final OfficeRepository officeRepository;
    private final WorkplaceMapper workplaceMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<WorkplaceResponse> findAll(PageRequest pageRequest) {
        return workplaceRepository.findAll(pageRequest).map(workplaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkplaceResponse findById(UUID id) {
        return workplaceMapper.toDto(workplaceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Workplace", id)
        ));
    }

    @Override
    @Transactional
    public WorkplaceResponse create(WorkplaceCreateRequest toCreate) {
        Workplace workplace = workplaceMapper.toEntity(toCreate);
        workplace.setOffice(officeRepository.findById(toCreate.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office",
                        toCreate.getOfficeId())));
        workplaceRepository.save(workplace);
        return workplaceMapper.toDto(workplace);
    }

    @Override
    @Transactional
    public WorkplaceResponse update(WorkplaceUpdateRequest toUpdate) {
        workplaceRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Workplace", toUpdate.getId())
        );
        Workplace workplace = workplaceMapper.toEntity(toUpdate);
        workplace.setOffice(officeRepository.findById(toUpdate.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office",
                        toUpdate.getOfficeId())));
        workplaceRepository.save(workplace);
        return workplaceMapper.toDto(workplace);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        workplaceRepository.deleteById(id);
    }
}
