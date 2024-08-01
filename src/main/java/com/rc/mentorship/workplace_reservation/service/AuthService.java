package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse register(RegisterRequest registerRequest);
}
