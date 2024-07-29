package com.rc.mentorship.workplace_reservation.controller.api;

import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
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

@Tag(name = "Reservations", description = "Бронирования")
public interface ReservationApi {
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
    ResponseEntity<Page<ReservationResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            Integer pageSize,
            @Parameter(hidden = true)
            Map<String, String> filters
    );

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
    ResponseEntity<ReservationResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );

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
    ResponseEntity<ReservationResponse> create(
            @RequestBody(description = "Запрос создания бронирования")
            ReservationCreateRequest createRequest
    );

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
    ResponseEntity<ReservationResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id,
            @RequestBody(description = "Запрос обновления бронирования")
            ReservationUpdateRequest updateRequest);

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
    ResponseEntity<Void> delete(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );
}
