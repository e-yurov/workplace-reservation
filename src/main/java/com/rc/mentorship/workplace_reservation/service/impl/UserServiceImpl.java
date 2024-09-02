package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.apiclient.UserClient;
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

    @Override
    @Cacheable(key = "#id", value = "users")
    public UserResponse getUserById(UUID id) {
        UserResponse userResponse;
        try {
            String token = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials())
                    .getTokenValue();
            userResponse = userClient.findById(id, "Bearer " + token).getBody();
        } catch (FeignException e) {
            if (e.status() == -1) {
                throw new InternalErrorException("User service is not responding!");
            }
            throw e;
        }
        return userResponse;
    }
}
