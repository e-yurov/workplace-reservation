package com.rc.mentorship.workplace_reservation.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Reservation extends EntityInMemory {
    private UUID userId;
    private UUID workplaceId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
