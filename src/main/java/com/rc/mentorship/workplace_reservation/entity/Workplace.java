package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Workplace {
    private long id;
    private long officeId;
    private int floor;
    private String type;
    private boolean computerPresent;
    private boolean available;
}
