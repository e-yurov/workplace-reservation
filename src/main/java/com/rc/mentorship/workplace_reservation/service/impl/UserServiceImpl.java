package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.apiclient.UserClient;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeIdResponse;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.exception.InternalErrorException;
import com.rc.mentorship.workplace_reservation.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserClient userClient;

    private final static String BEARER = "Bearer ";

    @Override
    @Cacheable(key = "#id", value = "users")
    public UserResponse getUserById(UUID id) {
        return userClient.findById(id, BEARER + getTokenFromAuth()).getBody();
    }

    @Override
    public OfficeIdResponse getOfficeIdByKeycloakId(UUID keycloakId) {
        return userClient.findOfficeIdByKeycloakId(keycloakId, BEARER + getTokenFromAuth()).getBody();
    }

    private String getTokenFromAuth() {
        return ((Jwt) SecurityContextHolder.getContext().getAuthentication()
                .getCredentials())
                .getTokenValue();
    }
}
