package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID>,
        JpaSpecificationExecutor<Reservation> {
    @Query(
            value = "SELECT exists(" +
                    "SELECT * FROM workplace_reservation.reservation r " +
                    "WHERE r.workplace_id = ?1 AND " +
                    "(r.start_date_time, r.end_date_time) OVERLAPS (?2, ?3))",
            nativeQuery = true
    )
    boolean checkReserved(UUID workplaceId, LocalDateTime startReservation, LocalDateTime endReservation);
}
