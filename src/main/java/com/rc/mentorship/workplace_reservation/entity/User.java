package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long id;
    private String name;
    private String email;
    private String role;
}
