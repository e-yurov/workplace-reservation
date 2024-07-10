package com.rc.mentorship.workplace_reservation.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationUpdateRequestDto {
    private long id;
    private String city;
    private String address;
}
