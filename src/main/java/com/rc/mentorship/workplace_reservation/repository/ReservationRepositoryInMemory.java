package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ReservationRepositoryInMemory extends RepositoryInMemory<Reservation, UUID> {

    public Reservation save(Reservation reservation) {
        return super.add(reservation, reservation.getId());
    }
}
