package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface UserService {
    Page<UserResponse> findAll(PageRequest pageRequest);

    UserResponse findById(UUID id);

    UserResponse update(UserUpdateRequest toUpdate);

    void delete(UUID id);

    UserResponse getUserById(UUID id);
}
