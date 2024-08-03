package com.rc.mentorship.workplace_reservation.security.auth;

public interface AuthenticationProvider {
    UserAuthentication authenticate(UserAuthentication authentication);
}
