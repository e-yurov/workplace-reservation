package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Workplace;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class WorkplaceRepositoryInMemory extends RepositoryInMemory<Workplace, UUID> {

    public Workplace save(Workplace workplace) {
        return super.add(workplace, workplace.getId());
    }
}
