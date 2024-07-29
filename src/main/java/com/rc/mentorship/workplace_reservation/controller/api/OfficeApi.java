package com.rc.mentorship.workplace_reservation.controller.api;

import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Offices", description = "Офисы")
public interface OfficeApi {
    @Operation(
            summary = "Получение всех офисов"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение всех офисов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeResponse.class)
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
                    name = "startTime",
                    description = "Фильтр по времени начала работы офиса",
                    example = "09:00:00"
            ),
            @Parameter(
                    name = "endTime",
                    description = "Фильтр по времени окончания работы офиса",
                    example = "18:00:00"
            ),
            @Parameter(
                    name = "locationId",
                    description = "Фильтр по id локации",
                    schema = @Schema(implementation = UUID.class)
            )
    })
    ResponseEntity<Page<OfficeResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            Integer pageSize,
            @Parameter(hidden = true)
            Map<String, String> filters
    );

    @Operation(
            summary = "Получение офиса по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение офиса",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено офиса по данному id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<OfficeResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );

    @Operation(
            summary = "Создание офиса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание офиса",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено локации, в которой находится офис, по данному locationId",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<OfficeResponse> create(
            @RequestBody(description = "Запрос создания офиса")
            OfficeCreateRequest createRequest
    );

    @Operation(
            summary = "Обновление офиса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление офиса",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено офиса по данному id или локации по locationId",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<OfficeResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id,
            @RequestBody(description = "Запрос обновления офиса")
            OfficeUpdateRequest updateRequest);

    @Operation(
            summary = "Удаление офиса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление офиса",
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
