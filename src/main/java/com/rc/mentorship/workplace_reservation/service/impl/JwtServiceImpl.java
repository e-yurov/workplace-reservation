package com.rc.mentorship.workplace_reservation.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtServiceImpl implements JwtService {
    private final Duration jwtLifetime;
    private final String secret;

    public JwtServiceImpl(@Value("${jwt.lifetime}") Duration jwtLifetime,
                          @Value("${jwt.secret}") String secret) {
        this.jwtLifetime = jwtLifetime;
        this.secret = secret;
    }

    public String generateToken(String email) {
        return JWT.create()
                .withClaim("email", email)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtLifetime))
                .withIssuer("workplace app")
                .sign(Algorithm.HMAC256(secret));
    }

    public String retrieveEmail(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("workplace app")
                .build();

        return verifier.verify(token).getClaim("email").asString();
    }
}
