package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.Office;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class OfficeRepositoryInMemory extends RepositoryInMemory<Office, UUID> {

    public Office save(Office office) {
        return super.add(office, office.getId());
    }
}
