package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OfficeControllerIT extends IntegrationTest {
    @Autowired
    public OfficeControllerIT(MockMvc mockMvc,
                              ObjectMapper objectMapper,
                              JwtService jwtService) {
        super(mockMvc, objectMapper, jwtService);
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql"})
    void findAll_NoFilters_ReturningPageOfOneOffice() throws Exception {
        mockMvc.perform(get("/api/v1/offices")
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content").isArray(),
                        jsonPath("$.content.length()").value(1),
                        jsonPath("$.content[0].id").value(ID),
                        jsonPath("$.content[0].startTime").value("08:00:00"),
                        jsonPath("$.content[0].endTime").value("18:00:00")
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_offices_filter.sql"})
    void findAll_HasFilters_ReturningFilteredPageOfOneOffice() throws Exception {
        mockMvc.perform(get("/api/v1/offices")
                .header(AUTHORIZATION, BEARER + token)
                .param("locationId", ID)
                .param("startTime", "gte/08:00:00")
                .param("endTime", "lt/19:00:00")
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.content").isArray(),
                jsonPath("$.content.length()").value(1),
                jsonPath("$.content[0].id").value(ID)
        );
    }

    @Test
    void findAll_WrongFiltersFormat_ReturningBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/offices")
                .header(AUTHORIZATION, BEARER + token)
                .param("startTime", "invalid")
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.message").value(StringEndsWith.endsWith("'startTime'!"))

        );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql"})
    void findById_HasOfficeById_ReturningOffice() throws Exception {
        mockMvc.perform(get("/api/v1/offices/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.startTime").value("08:00:00"),
                        jsonPath("$.endTime").value("18:00:00")
                );
    }

    @Test
    void findById_NoOfficeById_ReturningNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/offices/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Office", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void create_SimpleValues_ReturningCreatedOffice() throws Exception {
        OfficeCreateRequest request =
                new OfficeCreateRequest(UUID.fromString(ID), LocalTime.of(8, 0), LocalTime.of(18, 0));

        mockMvc.perform(post("/api/v1/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.location.id").value(ID),
                        jsonPath("$.startTime").value("08:00:00"),
                        jsonPath("$.endTime").value("18:00:00")
                );
    }

    @Test
    void create_NoLocation_RetuningNotFound() throws Exception {
        OfficeCreateRequest request =
                new OfficeCreateRequest(UUID.fromString(ID), LocalTime.of(8, 0), LocalTime.of(18, 0));

        mockMvc.perform(post("/api/v1/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Location", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql"})
    void update_SimpleValues_ReturningUpdatedOffice() throws Exception {
        OfficeUpdateRequest request = new OfficeUpdateRequest(UUID.fromString(ID), UUID.fromString(ID),
                LocalTime.of(7, 0), LocalTime.of(17, 0));

        mockMvc.perform(put("/api/v1/offices/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.location.id").value(ID),
                        jsonPath("$.startTime").value("07:00:00"),
                        jsonPath("$.endTime").value("17:00:00")
                );
    }

    @Test
    void update_NoOfficeToUpdate_ReturningNotFound() throws Exception {
        OfficeUpdateRequest request = new OfficeUpdateRequest(UUID.fromString(ID), UUID.fromString(ID),
                LocalTime.of(7, 0), LocalTime.of(17, 0));

        mockMvc.perform(put("/api/v1/offices/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Office", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/offices/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());
    }
}
