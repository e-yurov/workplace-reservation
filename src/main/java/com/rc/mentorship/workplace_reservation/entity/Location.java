package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Location extends EntityInMemory {
    private String city;
    private String address;
}
