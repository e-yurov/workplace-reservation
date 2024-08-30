package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Override
    @Cacheable(key = "#id", value = "users")
    public UserResponse getUserById(UUID id) {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        WebClient client = WebClient.builder().build();
        return client
                .get()
                .uri("http://localhost:8082/api/v1/users/" + id)
                .header("Authorization", "Bearer " + token.getTokenValue())
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }
}
