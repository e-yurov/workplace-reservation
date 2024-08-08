package com.rc.mentorship.workplace_reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OfficeResponse {
    private UUID id;
    private LocalTime startTime;
    private LocalTime endTime;
    @JsonProperty(value = "location")
    private LocationResponse locationResponse;
}
