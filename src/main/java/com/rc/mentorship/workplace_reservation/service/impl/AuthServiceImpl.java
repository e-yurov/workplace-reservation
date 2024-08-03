package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.LoginRequest;
import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.JwtResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.UserAlreadyExistsException;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.security.auth.AuthenticationProvider;
import com.rc.mentorship.workplace_reservation.security.auth.UserAuthentication;
import com.rc.mentorship.workplace_reservation.service.AuthService;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationProvider authProvider;

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }
        User user = userMapper.toEntity(registerRequest);
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return new JwtResponse(jwtService.generateToken(registerRequest.getEmail()));
    }

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest loginRequest) {
        UserAuthentication authentication = authProvider.authenticate(
                new UserAuthentication(loginRequest.getEmail(), loginRequest.getPassword())
        );
        return new JwtResponse(jwtService.generateToken(authentication.getEmail()));
    }
}
