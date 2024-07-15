package com.rc.mentorship.workplace_reservation.dto.request;

import com.rc.mentorship.workplace_reservation.entity.Workplace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkplaceUpdateRequest {
    private UUID id;
    private UUID officeId;
    private int floor;
    private Workplace.Type type;
    private boolean computerPresent;
    private boolean available;
}
