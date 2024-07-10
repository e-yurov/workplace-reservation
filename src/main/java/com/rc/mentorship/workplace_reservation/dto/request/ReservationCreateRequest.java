package com.rc.mentorship.workplace_reservation.dto.request;

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
public class ReservationCreateRequest {
    private UUID userId;
    private UUID workplaceId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
