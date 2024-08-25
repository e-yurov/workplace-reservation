package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {
    UserResponse fillUserResponse(UserResponse response);

    void addUser(RegisterRequest request);

    void updateUser(User user);

    void deleteUserById(String email);

    String getKeycloakIdByEmail(String email);

    UserRepresentation getKeycloakUserByEmail(String email);
}
