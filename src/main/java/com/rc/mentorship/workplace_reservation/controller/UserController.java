package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") UUID id) {
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserCreateRequest createRequest) {
        UserResponse response = userService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id,
                                                   @RequestBody UserUpdateRequest updateRequest) {
        updateRequest.setId(id);
        UserResponse response = userService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id){
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
