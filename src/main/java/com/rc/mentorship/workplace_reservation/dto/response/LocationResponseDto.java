package com.rc.mentorship.workplace_reservation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {
    private long id;
    private String city;
    private String address;
}
