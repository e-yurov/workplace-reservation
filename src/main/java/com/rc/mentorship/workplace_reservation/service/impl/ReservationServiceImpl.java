package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.BadReservationRequestException;
import com.rc.mentorship.workplace_reservation.exception.BadReservationTimeException;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundException;
import com.rc.mentorship.workplace_reservation.exception.WorkplaceNotAvailableException;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> findAll(PageRequest pageRequest) {
        return reservationRepository.findAll(pageRequest).map(reservationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse findById(UUID id) {
        return reservationMapper.toDto(reservationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Reservation", id)
        ));
    }

    @Override
    @Transactional
    public ReservationResponse create(ReservationCreateRequest toCreate) {
        Reservation reservation = reservationMapper.toEntity(toCreate);
        fillReservationOrThrow(reservation,
                toCreate.getUserId(), toCreate.getWorkplaceId(),
                toCreate.getStartDateTime(), toCreate.getEndDateTime());
        reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional
    public ReservationResponse update(ReservationUpdateRequest toUpdate) {
        reservationRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Reservation", toUpdate.getId())
        );
        Reservation reservation = reservationMapper.toEntity(toUpdate);
        fillReservationOrThrow(reservation,
                toUpdate.getUserId(), toUpdate.getWorkplaceId(),
                toUpdate.getStartDateTime(), toUpdate.getEndDateTime());
        reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        reservationRepository.deleteAll();
    }

    private void fillReservationOrThrow(Reservation reservation,
                                        UUID userId, UUID workplaceId,
                                        LocalDateTime start, LocalDateTime end) {
        if (reservation.getStartDateTime().isAfter(reservation.getEndDateTime())) {
            throw new BadReservationTimeException();
        }

        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workplace", workplaceId));
        checkAvailableAndNotReservedOrThrow(workplace.isAvailable(), workplaceId, start, end);

        reservation.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId)));
        reservation.setWorkplace(workplace);
    }

    private void checkAvailableAndNotReservedOrThrow(boolean available, UUID workplaceId,
                                                     LocalDateTime start, LocalDateTime end) {
        if (!available) {
            throw new WorkplaceNotAvailableException(workplaceId);
        }
        if (reservationRepository.checkReserved(workplaceId, start, end)) {
            throw new BadReservationRequestException(workplaceId);
        }
    }
}
