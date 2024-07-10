package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Location;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LocationRepositoryInMemory extends RepositoryInMemory<Location, UUID> {

    public Location save(Location location) {
        return super.add(location, location.getId());
    }
}
