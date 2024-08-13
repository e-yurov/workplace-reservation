package com.rc.mentorship.workplace_reservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.hamcrest.core.StringEndsWith;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReservationControllerIT extends IntegrationTest {
    @Autowired
    public ReservationControllerIT(MockMvc mockMvc,
                                   ObjectMapper objectMapper,
                                   JwtService jwtService) {
        super(mockMvc, objectMapper, jwtService);
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql", "/sql/insert_reservation.sql"})
    void findAll() throws Exception {
        mockMvc.perform(get("/api/v1/reservations")
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content").isArray(),
                        jsonPath("$.content.length()").value(1),
                        jsonPath("$.content[0].id").value(ID),
                        jsonPath("$.content[0].user.id").value(ID),
                        jsonPath("$.content[0].workplace.id").value(ID),
                        jsonPath("$.content[0].startDateTime").value("2024-07-18T12:00:00"),
                        jsonPath("$.content[0].endDateTime").value("2024-07-18T18:00:00")
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql", "/sql/insert_reservations_filters.sql"})
    void findAll_withFilters() throws Exception {
        mockMvc.perform(get("/api/v1/reservations")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("startDateTime", "gt/2024-07-18T11:00:00")
                        .param("endDateTime", "lte/2024-07-18T18:00:00")
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content").isArray(),
                        jsonPath("$.content.length()").value(1),
                        jsonPath("$.content[0].id").value(ID),
                        jsonPath("$.content[0].user.id").value(ID),
                        jsonPath("$.content[0].workplace.id").value(ID),
                        jsonPath("$.content[0].startDateTime").value("2024-07-18T12:00:00"),
                        jsonPath("$.content[0].endDateTime").value("2024-07-18T18:00:00")
                );
    }

    @Test
    void findAll_throwingFiltrationParamsFormatException() throws Exception {
        mockMvc.perform(get("/api/v1/reservations")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("startDateTime", "invalid")
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringEndsWith.endsWith("'startDateTime'!"))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql", "/sql/insert_reservation.sql"})
    void findById() throws Exception {
        mockMvc.perform(get("/api/v1/reservations/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.user.id").value(ID),
                        jsonPath("$.workplace.id").value(ID),
                        jsonPath("$.startDateTime").value("2024-07-18T12:00:00"),
                        jsonPath("$.endDateTime").value("2024-07-18T18:00:00")
                );
    }

    @Test
    void findById_throwingNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/reservations/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Reservation", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql"})
    void create() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T12:00:00"), LocalDateTime.parse("2024-07-18T18:00:00"));

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.user.id").value(ID),
                        jsonPath("$.workplace.id").value(ID),
                        jsonPath("$.startDateTime").value("2024-07-18T12:00:00"),
                        jsonPath("$.endDateTime").value("2024-07-18T18:00:00")
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql"})
    void create_throwingBadReservationTime() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T13:00:00"), LocalDateTime.parse("2024-07-18T12:00:00"));

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringStartsWith.startsWith("Wrong reservation time!"))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_not_available_workplace.sql", "/sql/insert_user.sql"})
    void create_throwingWorkplaceNotAvailable() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T12:00:00"), LocalDateTime.parse("2024-07-18T18:00:00"));

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringEndsWith.endsWith("is not available for reservation!"))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql", "/sql/insert_reservation.sql"})
    void create_throwingBadReservationRequest() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T10:00:00"), LocalDateTime.parse("2024-07-18T16:00:00"));

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringEndsWith.endsWith("already reserved!"))
                );
    }

    @Test
    void create_throwingNotFoundWorkplace() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T12:00:00"), LocalDateTime.parse("2024-07-18T18:00:00"));

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Workplace", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql", "/sql/insert_reservation.sql"})
    void update() throws Exception {
        ReservationUpdateRequest request = new ReservationUpdateRequest(UUID.fromString(ID),
                UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T13:00:00"), LocalDateTime.parse("2024-07-18T17:00:00"));

        mockMvc.perform(put("/api/v1/reservations/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.user.id").value(ID),
                        jsonPath("$.workplace.id").value(ID),
                        jsonPath("$.startDateTime").value("2024-07-18T13:00:00"),
                        jsonPath("$.endDateTime").value("2024-07-18T17:00:00")
                );
    }

    @Test
    void update_throwingNotFound() throws Exception {
        ReservationUpdateRequest request = new ReservationUpdateRequest(UUID.fromString(ID),
                UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T13:00:00"), LocalDateTime.parse("2024-07-18T17:00:00"));

        mockMvc.perform(put("/api/v1/reservations/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Reservation", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql",
            "/sql/insert_user.sql", "/sql/insert_reservation.sql", "/sql/insert_reservation_another.sql"})
    void update_throwingBadReservationRequest() throws Exception {
        ReservationUpdateRequest request = new ReservationUpdateRequest(UUID.fromString(ID),
                UUID.fromString(ID), UUID.fromString(ID),
                LocalDateTime.parse("2024-07-18T10:00:00"), LocalDateTime.parse("2024-07-18T16:00:00"));

        mockMvc.perform(put("/api/v1/reservations/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringEndsWith.endsWith("already reserved!"))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql",
            "/sql/insert_workplace.sql", "/sql/insert_user.sql", "/sql/insert_reservation.sql"})
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/reservations/" + ID)
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());
    }
}
