package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.KeycloakService;
import com.rc.mentorship.workplace_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest)
                .map(userMapper::toDto).map(keycloakService::fillUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        UserResponse response = userMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User", id)
        ));
        return keycloakService.fillUserResponse(response);
    }

    @Deprecated
    @Override
    @Transactional
    public UserResponse create(UserCreateRequest toCreate) {
        User user = userMapper.toEntity(toCreate);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse update(UserUpdateRequest toUpdate) {
        Optional<User> inDb = userRepository.findById(toUpdate.getId());
        if (inDb.isEmpty()) {
            throw new NotFoundException("User", toUpdate.getId());
        }
        User user = userMapper.toEntity(toUpdate);
        user.setKeycloakId(inDb.get().getKeycloakId());
        userRepository.save(user);
        keycloakService.updateUser(user);
        return keycloakService.fillUserResponse(userMapper.toDto(user));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return;
        }
        User user = userOptional.get();
        userRepository.delete(user);
        keycloakService.deleteUserById(user.getKeycloakId());
    }
}
