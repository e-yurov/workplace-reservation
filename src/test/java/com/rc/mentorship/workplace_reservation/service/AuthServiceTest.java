package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.JwtResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.UserAlreadyExistsException;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.MessageDigest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private MessageDigest messageDigest;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String TOKEN = "Token";
    private final String email = "Email";
    private final String password = "Password";
    private User user;
    private JwtResponse jwtResponse;

    @BeforeEach
    void beforeEach() {
        user = new User();
        jwtResponse = new JwtResponse(TOKEN);
    }

    @Test
    void register_SimpleValues_ReturningJwtToken() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(messageDigest.digest(password.getBytes())).thenReturn(new byte[]{});
        when(jwtService.generateToken(email)).thenReturn(TOKEN);

        fail("Need to change");
//        JwtResponse result = authService.register(request);
//
//        assertThat(result).isNotNull().isEqualTo(jwtResponse);
//        verify(userRepository, times(1)).save(user);
    }

    @Test
    void register_HasUserByEmail_ThrowingUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

//    @Test
//    void login_SimpleValues_ReturningJwtToken() {
//        LoginRequest request = new LoginRequest(email, password);
//        UserAuthentication authentication = new UserAuthentication(email, "");
//        when(messageDigest.digest(password.getBytes())).thenReturn(new byte[]{});
//        when(authProvider.authenticate(eq(authentication))).thenReturn(authentication);
//        when(jwtService.generateToken(email)).thenReturn(TOKEN);
//
//        JwtResponse result = authService.login(request);
//
//        assertThat(result).isNotNull().isEqualTo(jwtResponse);
//    }
}
