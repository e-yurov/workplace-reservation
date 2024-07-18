package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToReadException;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToUpdateException;
import com.rc.mentorship.workplace_reservation.mapper.WorkplaceMapper;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.WorkplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkplaceServiceImpl implements WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final OfficeRepository officeRepository;
    private final WorkplaceMapper workplaceMapper;

    @Override
    public List<WorkplaceResponse> findAll() {
        return workplaceRepository.findAll().stream().map(workplaceMapper::toDto).toList();
    }

    @Override
    public WorkplaceResponse findById(UUID id) {
        return workplaceMapper.toDto(workplaceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundToReadException("Workplace")
        ));
    }

    @Override
    public WorkplaceResponse create(WorkplaceCreateRequest toCreate) {
        Workplace workplace = workplaceMapper.toEntity(toCreate);
        workplace.setOffice(officeRepository.findById(toCreate.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundToReadException("Office")));
        workplaceRepository.save(workplace);
        return workplaceMapper.toDto(workplace);
    }

    @Override
    public WorkplaceResponse update(WorkplaceUpdateRequest toUpdate) {
        workplaceRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundToUpdateException("Workplace")
        );
        Workplace workplace = workplaceMapper.toEntity(toUpdate);
        workplace.setOffice(officeRepository.findById(toUpdate.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundToReadException("Office")));
        workplaceRepository.save(workplace);
        return workplaceMapper.toDto(workplace);
    }

    @Override
    public void delete(UUID id) {
        workplaceRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        workplaceRepository.deleteAll();
    }
}
