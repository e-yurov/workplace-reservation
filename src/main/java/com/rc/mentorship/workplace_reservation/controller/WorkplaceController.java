package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.service.WorkplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workplaces")
@RequiredArgsConstructor
public class WorkplaceController {
    private final WorkplaceService workplaceService;

    @GetMapping
    public ResponseEntity<List<WorkplaceResponse>> findAll() {
        return ResponseEntity.ok(workplaceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkplaceResponse> findById(@PathVariable("id") UUID id) {
        WorkplaceResponse response = workplaceService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<WorkplaceResponse> create(@RequestBody WorkplaceCreateRequest createRequest) {
        WorkplaceResponse response = workplaceService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkplaceResponse> update(@PathVariable("id") UUID id,
                                                   @RequestBody WorkplaceUpdateRequest updateRequest) {
        updateRequest.setId(id);
        WorkplaceResponse response = workplaceService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id){
        workplaceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
