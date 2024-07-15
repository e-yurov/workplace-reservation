package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepositoryInMemory;
import com.rc.mentorship.workplace_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryInMemory userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserResponse findById(UUID id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public UserResponse create(UserCreateRequest toCreate) {
        User user = userMapper.toEntity(toCreate);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponse update(UserUpdateRequest toUpdate) {
        userRepository.findById(toUpdate.getId()).orElseThrow(RuntimeException::new);
        User user = userMapper.toEntity(toUpdate);
        userRepository.update(user);
        return userMapper.toDto(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
