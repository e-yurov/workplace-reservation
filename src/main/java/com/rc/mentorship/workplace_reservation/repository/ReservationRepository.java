package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID>,
        JpaSpecificationExecutor<Reservation> {
    @Query("""
           FROM Reservation r
           WHERE (r.workplace.id = :workplaceId) AND (
               ((r.startDateTime >= :startReservation) AND (r.startDateTime < :endReservation)) OR
               ((r.endDateTime > :startReservation) AND (r.endDateTime <= :endReservation)) OR
               ((r.startDateTime <= :startReservation) AND (r.endDateTime >= :endReservation))
           )
           """
    )
    List<Reservation> checkReserved(UUID workplaceId,
                                    LocalDateTime startReservation, LocalDateTime endReservation);
}
