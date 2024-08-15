package com.rc.mentorship.workplace_reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WorkplaceResponse {
    private UUID id;
    private int floor;
    private Workplace.Type type;
    private boolean computerPresent;
    private boolean available;
    @JsonProperty(value = "office")
    private OfficeResponse officeResponse;
}
