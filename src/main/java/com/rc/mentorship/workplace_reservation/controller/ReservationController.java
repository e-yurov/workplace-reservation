package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.controller.api.ReservationApi;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController implements ReservationApi {
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<ReservationResponse>> findAll(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam Map<String, String> filters
    ) {
        return ResponseEntity.ok(reservationService.findAllWithFilters(
                PageRequest.of(pageNumber, pageSize),
                filters
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> findById(@PathVariable("id") UUID id) {
        ReservationResponse response = reservationService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationCreateRequest createRequest) {
        ReservationResponse response = reservationService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> update(@PathVariable("id") UUID id,
                                                   @RequestBody ReservationUpdateRequest updateRequest) {
        updateRequest.setId(id);
        ReservationResponse response = reservationService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id){
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
