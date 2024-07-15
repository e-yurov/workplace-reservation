package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepositoryInMemory;
import com.rc.mentorship.workplace_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepositoryInMemory reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
    }

    @Override
    public ReservationResponse findById(UUID id) {
        return reservationMapper.toDto(reservationRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public ReservationResponse create(ReservationCreateRequest toCreate) {
        Reservation reservation = reservationMapper.toEntity(toCreate);
        reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
    public ReservationResponse update(ReservationUpdateRequest toUpdate) {
        reservationRepository.findById(toUpdate.getId()).orElseThrow(RuntimeException::new);
        Reservation reservation = reservationMapper.toEntity(toUpdate);
        reservationRepository.update(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
    public void delete(UUID id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        reservationRepository.deleteAll();
    }
}
