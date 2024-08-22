package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import com.rc.mentorship.workplace_reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Бронирования")
@SecurityRequirement(name = "Keycloak")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(
            summary = "Получение всех бронирований"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение всех бронирований",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неправильный формат фильтров",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    @Parameters({
            @Parameter(
                    name = "startDateTime",
                    description = "Фильтр по времени начала бронирования",
                    example = "2024-07-18T12:00:00"
            ),
            @Parameter(
                    name = "endDateTime",
                    description = "Фильтр по времени окончания бронирования",
                    example = "2024-07-18T20:00:00"
            ),
            @Parameter(
                    name = "userId",
                    description = "Фильтр по id пользователя",
                    schema = @Schema(implementation = UUID.class)
            ),
            @Parameter(
                    name = "workplaceId",
                    description = "Фильтр по id рабочего места",
                    schema = @Schema(implementation = UUID.class)
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ReservationResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            @RequestParam(defaultValue = "0")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            @RequestParam(defaultValue = "5")
            Integer pageSize,
            @Parameter(hidden = true)
            @RequestParam
            Map<String, String> filters
    ) {
        return ResponseEntity.ok(reservationService.findAllWithFilters(
                PageRequest.of(pageNumber, pageSize),
                filters
        ));
    }

    @Operation(
            summary = "Получение бронирования по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение бронирования",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено бронирования по данному id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReservationResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id
    ) {
        ReservationResponse response = reservationService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Создание бронирования"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание бронирования",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невозможно забронировать указанное рабочее место на это время",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено пользователя по userId или рабочего места по workplaceId",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReservationResponse> create(
            @RequestBody
            ReservationCreateRequest createRequest
    ) {
        ReservationResponse response = reservationService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Обновление бронирования"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление бронирования",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено бронирования по данному id," +
                            " либо пользователя по userId или рабочего места по workplaceId",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ReservationResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id,
            @RequestBody
            ReservationUpdateRequest updateRequest
    ) {
        updateRequest.setId(id);
        ReservationResponse response = reservationService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Удаление бронирования"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление бронирования",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    }
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id
    ){
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
