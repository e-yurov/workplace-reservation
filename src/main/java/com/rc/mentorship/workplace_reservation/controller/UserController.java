package com.rc.mentorship.workplace_reservation.controller;

import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import com.rc.mentorship.workplace_reservation.security.role.HasRole;
import com.rc.mentorship.workplace_reservation.security.role.Role;
import com.rc.mentorship.workplace_reservation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Пользователи")
@SecurityRequirement(name = "Keycloak")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Получение всех пользователей"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение всех пользователей",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    }
            )
    })
    @GetMapping
    @HasRole({Role.MANAGER, Role.ADMIN})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Page<UserResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            @RequestParam(defaultValue = "0")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            @RequestParam(defaultValue = "10")
            Integer pageSize,
            @Parameter(name = "role", description = "Фильтр по роли")
            @RequestParam(required = false)
            Role role
    ) {
        return ResponseEntity.ok(userService.findAllByRole(
                PageRequest.of(pageNumber, pageSize), role));
    }

    @Operation(
            summary = "Получение пользователя по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено пользователя по данному id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    @GetMapping("/{id}")
    @HasRole({Role.MANAGER, Role.ADMIN})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<UserResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id
    ) {
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Обновление пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено пользователя по данному id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)
                            )
                    }
            )
    })
    @PutMapping("/{id}")
    @HasRole(Role.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id,
            @RequestBody
            UserUpdateRequest updateRequest
    ) {
        updateRequest.setId(id);
        UserResponse response = userService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Удаление пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    }
            )
    })
    @DeleteMapping("/{id}")
    @HasRole(Role.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", in = ParameterIn.PATH)
            @PathVariable("id")
            UUID id
    ){
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
