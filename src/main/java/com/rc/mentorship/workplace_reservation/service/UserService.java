package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse getUserById(UUID id);
}
