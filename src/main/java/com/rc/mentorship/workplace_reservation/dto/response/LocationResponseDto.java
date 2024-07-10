package com.rc.mentorship.workplace_reservation.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {
    private UUID id;
    private String city;
    private String address;
}
