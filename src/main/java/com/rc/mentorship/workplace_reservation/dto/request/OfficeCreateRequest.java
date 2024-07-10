package com.rc.mentorship.workplace_reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfficeCreateRequest {
    private UUID locationId;
    private LocalTime startTime;
    private LocalTime endTime;
}
