package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private final String secret = "secret";
    private final Duration duration = Duration.of(30, ChronoUnit.MINUTES);
    private final String email = "email";

    private final JwtService jwtService = new JwtServiceImpl(duration, secret);

    @Test
    void generateToken_SimpleValues_RetuningToken() {
        String token = jwtService.generateToken(email);

        assertThat(token).isNotBlank();
    }

    @Test
    void verifyAndRetrieveEmail_ValidToken_RetuningEmail() {
        String token = jwtService.generateToken(email);

        String result = jwtService.verifyAndRetrieveEmail(token);

        assertThat(result).isNotBlank().isEqualTo(email);
    }
}
