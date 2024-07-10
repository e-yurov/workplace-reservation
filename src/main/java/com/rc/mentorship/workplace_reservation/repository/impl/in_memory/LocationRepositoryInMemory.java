package com.rc.mentorship.workplace_reservation.repository.impl.in_memory;

import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepositoryInMemory extends RepositoryInMemory<Location, Long>
        implements LocationRepository {
    @Override
    public Location save(Location item) {
        return super.add(item, item.getId());
    }
}
