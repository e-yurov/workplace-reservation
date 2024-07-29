package com.rc.mentorship.workplace_reservation.controller.api;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
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

@Tag(name = "Users", description = "Пользователи")
public interface UserApi {
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
    ResponseEntity<Page<UserResponse>> findAll(
            @Parameter(name = "pageNumber", description = "Номер страницы")
            Integer pageNumber,
            @Parameter(name = "pageSize", description = "Размер страницы")
            Integer pageSize,
            @Parameter(name = "role", description = "Фильтр по роли")
            String role
    );

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
    ResponseEntity<UserResponse> findById(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );

    @Operation(
            summary = "Создание пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    }
            )
    })
    ResponseEntity<UserResponse> create(
            @RequestBody(description = "Запрос создания пользователя")
            UserCreateRequest createRequest
    );

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
    ResponseEntity<UserResponse> update(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id,
            @RequestBody(description = "Запрос обновления пользователя")
            UserUpdateRequest updateRequest);

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
    ResponseEntity<Void> delete(
            @Parameter(name = "id", in = ParameterIn.PATH)
            UUID id
    );
}
