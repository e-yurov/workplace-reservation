package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User extends EntityInMemory {
    private String name;
    private String email;
    private String role;
}
