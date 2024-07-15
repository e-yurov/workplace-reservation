package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class Office extends EntityInMemory {
    private UUID locationId;
    private LocalTime startTime;
    private LocalTime endTime;
}
