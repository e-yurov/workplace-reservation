package com.rc.mentorship.workplace_reservation.auth;

import com.rc.mentorship.workplace_reservation.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserAuthentication {
    private final String email;
    private final String password;
    @Setter
    private User user;

    public UserAuthentication(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
