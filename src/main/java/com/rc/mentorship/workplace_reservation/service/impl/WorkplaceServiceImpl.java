package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.WorkplaceMapper;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.WorkplaceService;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.converter.impl.WorkplaceFilterConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkplaceServiceImpl implements WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final OfficeRepository officeRepository;
    private final WorkplaceMapper workplaceMapper;
    private final WorkplaceFilterConverter workplaceFilterConverter;

    @Override
    @Transactional(readOnly = true)
    public Page<WorkplaceResponse> findAll(PageRequest pageRequest) {
        return workplaceRepository.findAll(pageRequest).map(workplaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkplaceResponse> findAllWithFilters(PageRequest pageRequest,
                                                      Map<String, Filter> fieldFilterMap) {
        List<WorkplaceResponse> response = workplaceRepository.findAll().stream()
                .filter(workplaceFilterConverter.convert(fieldFilterMap))
                .map(workplaceMapper::toDto).toList();
        return new PageImpl<>(response, pageRequest, response.size());
    }

    @Override
    @Transactional(readOnly = true)
    public WorkplaceResponse findById(UUID id) {
        return workplaceMapper.toDto(workplaceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Workplace", id)
        ));
    }

    @Override
    @Transactional
    public WorkplaceResponse create(WorkplaceCreateRequest toCreate) {
        Workplace workplace = workplaceMapper.toEntity(toCreate);
        workplace.setOffice(officeRepository.findById(toCreate.getOfficeId())
                .orElseThrow(() -> new NotFoundException("Office",
                        toCreate.getOfficeId())));
        workplaceRepository.save(workplace);
        return workplaceMapper.toDto(workplace);
    }

    @Override
    @Transactional
    public WorkplaceResponse update(WorkplaceUpdateRequest toUpdate) {
        workplaceRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new NotFoundException("Workplace", toUpdate.getId())
        );
        Workplace workplace = workplaceMapper.toEntity(toUpdate);
        workplace.setOffice(officeRepository.findById(toUpdate.getOfficeId())
                .orElseThrow(() -> new NotFoundException("Office",
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
