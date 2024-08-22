package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;

public interface KeycloakService {
    void addUser(RegisterRequest request);
}
