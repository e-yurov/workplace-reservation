package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.response.UserResponseDto;
import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequestDto;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequestDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();
    UserResponseDto findById(long id);
    UserResponseDto create(UserCreateRequestDto toCreate);
    UserResponseDto update(UserUpdateRequestDto toUpdate);
    void delete(long id);
    void deleteAll();
}
