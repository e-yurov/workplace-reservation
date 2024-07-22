package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAllByRole(PageRequest pageRequest, String role) {
        return userRepository.findByRole(role, pageRequest).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", id)
        ));
    }

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
        userRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", toUpdate.getId())
        );
        User user = userMapper.toEntity(toUpdate);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
