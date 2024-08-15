package com.rc.mentorship.workplace_reservation.security.auth;

import com.rc.mentorship.workplace_reservation.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class UserAuthentication {
    private final String email;
    private final String password;
    @Setter
    private User user;

    public UserAuthentication(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserAuthentication(String email, String password, User user) {
        this.email = email;
        this.password = password;
        this.user = user;
    }
}
