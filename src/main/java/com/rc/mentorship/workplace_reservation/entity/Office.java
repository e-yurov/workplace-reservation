package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class Office {
    private long id;
    private long locationId;
    private LocalTime startTime;
    private LocalTime endTime;
}
