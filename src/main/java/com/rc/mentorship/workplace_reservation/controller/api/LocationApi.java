package com.rc.mentorship.workplace_reservation.controller.api;

import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Locations", description = "Локации офисов")
public interface LocationApi {
    @Operation(
            summary = "Получение всех локаций"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение всех локаций",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LocationResponse.class)
                            )
                    }
            )
    })
    ResponseEntity<Page<LocationResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            Integer pageSize,
            @Parameter(name = "city",
                    description = "Город, по которому результат будет отфильтрован")
            String city
    );

    @Operation(
            summary = "Получение локации по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение локации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LocationResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено локации по данному id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<LocationResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );

    @Operation(
            summary = "Создание локации"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание локации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LocationResponse.class)
                            )
                    }
            )
    })
    ResponseEntity<LocationResponse> create(
            @RequestBody(description = "Запрос создания локации")
            LocationCreateRequest createRequest
    );

    @Operation(
            summary = "Обновление локации"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление локации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LocationResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено локации по данному id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<LocationResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id,
            @RequestBody(description = "Запрос обновления локации")
            LocationUpdateRequest updateRequest);

    @Operation(
            summary = "Удаление локации"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление локации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    }
            )
    })
    ResponseEntity<Void> delete(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );
}
