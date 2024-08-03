package com.rc.mentorship.workplace_reservation.security.context;

import com.rc.mentorship.workplace_reservation.security.auth.UserAuthentication;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SecurityContext {
    private UserAuthentication authentication;
}
