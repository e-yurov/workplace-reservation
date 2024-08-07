package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import com.rc.mentorship.workplace_reservation.security.role.HasRole;
import com.rc.mentorship.workplace_reservation.security.role.Role;
import com.rc.mentorship.workplace_reservation.service.WorkplaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workplaces")
@RequiredArgsConstructor
@Tag(name = "Workplaces", description = "Рабочие места")
public class WorkplaceController {
    private final WorkplaceService workplaceService;

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
    @GetMapping
    @HasRole
    public ResponseEntity<Page<WorkplaceResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            @RequestParam(defaultValue = "0")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            @RequestParam(defaultValue = "10")
            Integer pageSize,
            @RequestParam
            String officeId,
            @Parameter(hidden = true)
            @RequestParam
            Map<String, String> filters
    ) {
        return ResponseEntity.ok(workplaceService.findAllWithFilters(
                PageRequest.of(pageNumber, pageSize),
                filters
        ));
    }

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
    @GetMapping("/{id}")
    @HasRole
    public ResponseEntity<WorkplaceResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id
    ) {
        WorkplaceResponse response = workplaceService.findById(id);
        return ResponseEntity.ok(response);
    }

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
    @PostMapping
    @HasRole({Role.MANAGER, Role.ADMIN})
    public ResponseEntity<WorkplaceResponse> create(
            @RequestBody
            WorkplaceCreateRequest createRequest
    ) {
        WorkplaceResponse response = workplaceService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

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
    @PutMapping("/{id}")
    @HasRole({Role.MANAGER, Role.ADMIN})
    public ResponseEntity<WorkplaceResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id,
            @RequestBody
            WorkplaceUpdateRequest updateRequest
    ) {
        updateRequest.setId(id);
        WorkplaceResponse response = workplaceService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

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
    @DeleteMapping("/{id}")
    @HasRole({Role.MANAGER, Role.ADMIN})
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id
    ){
        workplaceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
