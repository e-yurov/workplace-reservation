package com.rc.mentorship.workplace_reservation.controller.api;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
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

@Tag(name = "Workplaces", description = "Рабочие места")
public interface WorkplaceApi {

    @Operation(
            summary = "Получение всех рабочих мест"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение всех рабочих мест",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WorkplaceResponse.class)
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
                    name = "floor",
                    description = "Фильтр по этажу",
                    schema = @Schema(implementation = Integer.class)
            ),
            @Parameter(
                    name = "type",
                    description = "Фильтр по типу рабочего места",
                    schema = @Schema(implementation = Workplace.Type.class)
            ),
            @Parameter(
                    name = "computerPresent",
                    description = "Фильтр по наличию компьютера",
                    schema = @Schema(implementation = Boolean.class)
            ),
            @Parameter(
                    name = "available",
                    description = "Фильтр по доступности рабочего места",
                    schema = @Schema(implementation = Boolean.class)
            ),
            @Parameter(
                    name = "officeId",
                    description = "Фильтр по id офиса",
                    schema = @Schema(implementation = UUID.class)
            )
    })
    ResponseEntity<Page<WorkplaceResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            Integer pageSize,
            @Parameter(hidden = true)
            Map<String, String> filters
    );

    @Operation(
            summary = "Получение рабочего места по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение рабочего места",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WorkplaceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено рабочего места по данному id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<WorkplaceResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );

    @Operation(
            summary = "Создание рабочего места"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание рабочего места",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WorkplaceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено офиса по officeId",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<WorkplaceResponse> create(
            @RequestBody(description = "Запрос создания рабочего места")
            WorkplaceCreateRequest createRequest
    );

    @Operation(
            summary = "Обновление рабочего места"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление рабочего места",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WorkplaceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено рабочего места по данному id или офиса по officeId",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    ResponseEntity<WorkplaceResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id,
            @RequestBody(description = "Запрос обновления рабочего места")
            WorkplaceUpdateRequest updateRequest);

    @Operation(
            summary = "Удаление рабочего места"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление рабочего места",
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
