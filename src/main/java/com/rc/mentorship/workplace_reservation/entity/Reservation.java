package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Reservation {
    private long id;
    private long userId;
    private long workplaceId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
