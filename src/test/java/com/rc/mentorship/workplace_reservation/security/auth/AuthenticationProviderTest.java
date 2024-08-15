package com.rc.mentorship.workplace_reservation.security.auth;

import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.BadCredentialsException;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationProviderTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationProviderImpl authProvider;

    private final String email = "Email";
    private final String password = "Password";
    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
    }

    @Test
    void authenticate_SimpleValues_ReturningAuthentication() {
        UserAuthentication authentication = new UserAuthentication(email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserAuthentication result = authProvider.authenticate(authentication);

        assertThat(result).isNotNull().extracting(auth -> auth.getUser().getEmail(),
                auth -> auth.getUser().getPassword())
                .containsExactly(email, password);
    }

    @Test
    void authenticate_WrongEmail_ThrowingBadCredentials() {
        UserAuthentication authentication = new UserAuthentication(email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authProvider.authenticate(authentication))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void authenticate_WrongPassword_ThrowingBadCredentials() {
        UserAuthentication authentication = new UserAuthentication(email, password);
        user.setPassword("123");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authProvider.authenticate(authentication))
                .isInstanceOf(BadCredentialsException.class);
    }
}
