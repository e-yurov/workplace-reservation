package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Workplace {
    private UUID id;
    private UUID officeId;
    private int floor;
    private String type;
    private boolean computerPresent;
    private boolean available;
}
