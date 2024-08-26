package com.rc.mentorship.workplace_reservation.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMessage {
    private String email;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private UUID workplaceId;
}
