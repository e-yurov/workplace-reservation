package com.rc.mentorship.workplace_reservation.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private List<String> roles;
}
