package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.controller.api.UserApi;
import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String role
            ) {
        return ResponseEntity.ok(userService.findAllByRole(
                PageRequest.of(pageNumber, pageSize), role));
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
