package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.UserAlreadyExistsException;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.AuthService;
import com.rc.mentorship.workplace_reservation.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }
        User user = userMapper.toEntity(registerRequest);
//        user.setPassword(encryptPassword(registerRequest.getPassword()));
        user.setRole(User.Role.USER);
        userRepository.save(user);
        keycloakService.addUser(registerRequest);
        return userMapper.toDto(user);
//        return new JwtResponse(jwtService.generateToken(email));
    }

//    private String encryptPassword(String password) {
//        byte[] digest = messageDigest.digest(password.getBytes());
//        return Base64.getEncoder().encodeToString(digest);
//    }
}
