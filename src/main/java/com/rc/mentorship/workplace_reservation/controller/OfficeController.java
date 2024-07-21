package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offices")
@RequiredArgsConstructor
public class OfficeController {
    private final OfficeService officeService;

    @GetMapping
    public ResponseEntity<Page<OfficeResponse>> findAll(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(officeService.findAll(PageRequest.of(pageNumber, pageSize)));
    }

    @GetMapping("{id}")
    public ResponseEntity<OfficeResponse> findById(@PathVariable("id") UUID id) {
        OfficeResponse response = officeService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OfficeResponse> create(@RequestBody OfficeCreateRequest createRequest) {
        OfficeResponse response = officeService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<OfficeResponse> update(@PathVariable("id") UUID id,
                                                 @RequestBody OfficeUpdateRequest updateRequest) {
        updateRequest.setId(id);
        OfficeResponse response = officeService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        officeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
