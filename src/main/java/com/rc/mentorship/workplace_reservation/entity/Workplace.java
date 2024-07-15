package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Workplace extends EntityInMemory {
    private UUID officeId;
    private int floor;
    private Type type;
    private boolean computerPresent;
    private boolean available;

    public enum Type {
        DESK,
        ROOM
    }
}
