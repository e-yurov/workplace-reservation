package com.rc.mentorship.workplace_reservation.service;

public interface JwtService {
    String generateToken(String email);

    String verifyAndRetrieveEmail(String token);
}
