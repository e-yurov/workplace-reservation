package com.rc.mentorship.workplace_reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkplaceResponse {
    private UUID id;
    private UUID officeId;
    private int floor;
    private String type;
    private boolean computerPresent;
    private boolean available;
}
