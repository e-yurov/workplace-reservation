package com.rc.mentorship.workplace_reservation.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationUpdateRequestDto {
    private UUID id;
    private String city;
    private String address;
}
