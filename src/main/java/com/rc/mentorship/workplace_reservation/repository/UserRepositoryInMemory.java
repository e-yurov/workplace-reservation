package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepositoryInMemory extends RepositoryInMemory<User, UUID> {

    public User save(User user) {
        return super.add(user, user.getId());
    }
}
