package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToReadException;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundToUpdateException;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ReservationMapper reservationMapper;

    //TODO: add Transactional
    @Override
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
    }

    @Override
    public ReservationResponse findById(UUID id) {
        return reservationMapper.toDto(reservationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundToReadException("Reservation")
        ));
    }

    @Override
    public ReservationResponse create(ReservationCreateRequest toCreate) {
        Reservation reservation = reservationMapper.toEntity(toCreate);
        fillReservation(reservation, toCreate.getUserId(), toCreate.getWorkplaceId());
        reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
    public ReservationResponse update(ReservationUpdateRequest toUpdate) {
        reservationRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundToUpdateException("Reservation")
        );
        Reservation reservation = reservationMapper.toEntity(toUpdate);
        fillReservation(reservation, toUpdate.getUserId(), toUpdate.getWorkplaceId());
        reservationRepository.save(reservation);
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

    private void fillReservation(Reservation reservation, UUID userId, UUID workplaceId) {
        reservation.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundToReadException("User")));
        reservation.setWorkplace(workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new ResourceNotFoundToReadException("Workplace")));
    }
}
