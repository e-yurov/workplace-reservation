package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Location;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepositoryInMemory extends RepositoryInMemory<Location, Long> {

    public Location save(Location location) {
        return super.add(location, location.getId());
    }
}
