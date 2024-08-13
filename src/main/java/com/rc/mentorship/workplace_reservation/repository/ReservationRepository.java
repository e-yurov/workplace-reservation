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
           WHERE (r.workplace.id = :workplaceId) AND
           ((:updatedId IS NULL) OR (r.id != :updatedId)) AND (
               ((r.dateTime.start >= :startReservation) AND (r.dateTime.start < :endReservation)) OR
               ((r.dateTime.end > :startReservation) AND (r.dateTime.end <= :endReservation)) OR
               ((r.dateTime.start <= :startReservation) AND (r.dateTime.end >= :endReservation))
           )
           """
    )
    List<Reservation> checkReserved(UUID workplaceId, UUID updatedId,
                                    LocalDateTime startReservation, LocalDateTime endReservation);
}
