package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface UserService {
    Page<UserResponse> findAllByRole(PageRequest pageRequest, User.Role role);

    UserResponse findById(UUID id);

    UserResponse create(UserCreateRequest toCreate);

    UserResponse update(UserUpdateRequest toUpdate);

    void delete(UUID id);
}
