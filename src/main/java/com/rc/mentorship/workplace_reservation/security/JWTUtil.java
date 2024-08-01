package com.rc.mentorship.workplace_reservation.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

public class JWTUtil {
    private final Duration jwtLifetime;
    private final String secret;

    public JWTUtil(@Value("${jwt.lifetime}") Duration jwtLifetime,
                   @Value("${jwt.secret}") String secret) {
        this.jwtLifetime = jwtLifetime;
        this.secret = secret;
    }

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtLifetime))
                .withIssuer("workplace app")
                .sign(Algorithm.HMAC256(secret));

    }

    public String retrieveClaim(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("workplace app")
                .build();

        return verifier.verify(token).getSubject();
    }
}
