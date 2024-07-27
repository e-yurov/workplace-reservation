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
    @Query("FROM Reservation r " +
            "WHERE (r.workplace.id = ?1) AND (" +
                "((r.startDateTime >= ?2) AND (r.startDateTime < ?3)) OR " +
                "((r.endDateTime > ?2) AND (r.endDateTime <= ?3)) OR " +
                "((r.startDateTime <= ?2) AND (r.endDateTime >= ?3)))"
    )
    List<Reservation> checkReserved(UUID workplaceId,
                                    LocalDateTime startReservation, LocalDateTime endReservation);
}
