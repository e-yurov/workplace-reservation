package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String NAME = "name";
    private static final String EMAIL = "email@test.com";
    private static final String PASSWORD = "Password";
    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
    }

    @Test
    void register_SimpleValues_ReturningJwtToken() {
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, PASSWORD);
        UserResponse expected = new UserResponse();
        expected.setName(NAME);
        expected.setEmail(EMAIL);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);

        UserResponse result = authService.register(request);

        assertThat(result).isNotNull()
                .extracting(UserResponse::getName, UserResponse::getEmail)
                .containsExactly(NAME, EMAIL);
        verify(keycloakService, times(1)).addUser(request);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void register_HasUserByEmail_ThrowingUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, PASSWORD);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }
}
