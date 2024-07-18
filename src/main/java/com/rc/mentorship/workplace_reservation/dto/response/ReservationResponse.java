package com.rc.mentorship.workplace_reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ReservationResponse {
    private UUID id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @JsonProperty(value = "user")
    private UserResponse userResponse;
    @JsonProperty(value = "workplace")
    private WorkplaceResponse workplaceResponse;
}
