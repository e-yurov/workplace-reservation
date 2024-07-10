package com.rc.mentorship.workplace_reservation.dto.response;

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
public class OfficeResponse {
    private UUID id;
    private UUID locationId;
    private LocalTime startTime;
    private LocalTime endTime;
}
