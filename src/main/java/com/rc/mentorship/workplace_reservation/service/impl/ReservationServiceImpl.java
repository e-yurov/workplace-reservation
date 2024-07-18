package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
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
        fillReservation(reservation, toCreate.getUserId(), toCreate.getWorkplaceId());
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
        fillReservation(reservation, toUpdate.getUserId(), toUpdate.getWorkplaceId());
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

    private void fillReservation(Reservation reservation, UUID userId, UUID workplaceId) {
        reservation.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId)));
        reservation.setWorkplace(workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workplace", workplaceId)));
    }
}
