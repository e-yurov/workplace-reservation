package com.rc.mentorship.workplace_reservation.auth;

public interface AuthenticationProvider {
    UserAuthentication authenticate(UserAuthentication authentication);
}
