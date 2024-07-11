package com.rc.mentorship.workplace_reservation.mapper;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateRequest requestDto);

    User toEntity(UserUpdateRequest requestDto);

    UserResponse toDto(User entity);
}
