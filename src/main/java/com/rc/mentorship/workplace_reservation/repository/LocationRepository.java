package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
}
