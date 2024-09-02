package com.rc.mentorship.workplace_reservation.apiclient;

import com.rc.mentorship.workplace_reservation.dto.response.OfficeIdResponse;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        name = "${feign.users.name}",
        url = "${feign.users.url}"
)
public interface UserClient {
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(
            @PathVariable("id") UUID id,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/office-id")
    ResponseEntity<OfficeIdResponse> findOfficeIdByKeycloakId(
            @RequestParam UUID keycloakId,
            @RequestHeader("Authorization") String token
    );
}
