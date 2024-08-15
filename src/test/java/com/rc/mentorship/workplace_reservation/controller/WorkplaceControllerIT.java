package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//todo: add assertions for Location and Office
public class WorkplaceControllerIT extends IntegrationTest {
    @Autowired
    public WorkplaceControllerIT(MockMvc mockMvc,
                                 ObjectMapper objectMapper,
                                 JwtService jwtService) {
        super(mockMvc, objectMapper, jwtService);
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql"})
    void findAll_NoFilters_ReturningPageOfOneWorkplace() throws Exception {
        mockMvc.perform(get("/api/v1/workplaces")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", ID)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content").isArray(),
                        jsonPath("$.content.length()").value(1),
                        jsonPath("$.content[0].id").value(ID),
                        jsonPath("$.content[0].floor").value(1),
                        jsonPath("$.content[0].type").value("DESK"),
                        jsonPath("$.content[0].computerPresent").value(true),
                        jsonPath("$.content[0].available").value(true)
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplaces_filter.sql"})
    void findAll_HasFilters_ReturningFilteredPageOfOneWorkplace() throws Exception {
        mockMvc.perform(get("/api/v1/workplaces")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", ID)
                        .param("floor", "lt/2")
                        .param("type", "DESK")
                        .param("computerPresent", "true")
                        .param("available", "true")
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content").isArray(),
                        jsonPath("$.content.length()").value(1),
                        jsonPath("$.content[0].id").value(ID),
                        jsonPath("$.content[0].floor").value(1),
                        jsonPath("$.content[0].type").value("DESK"),
                        jsonPath("$.content[0].computerPresent").value(true),
                        jsonPath("$.content[0].available").value(true)
                );
    }

    @Test
    void findAll_WrongFiltersFormat_ReturningBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/workplaces")
                .header(AUTHORIZATION, BEARER + token)
                .param("officeId", ID)
                .param("floor", "aa/invalid")
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.message").value(StringEndsWith.endsWith("'floor'!"))
        );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql"})
    void findById_HasWorkplaceById_ReturningWorkplace() throws Exception {
        mockMvc.perform(get("/api/v1/workplaces/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", ID)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.floor").value(1),
                        jsonPath("$.type").value("DESK"),
                        jsonPath("$.computerPresent").value(true),
                        jsonPath("$.available").value(true)
                );
    }

    @Test
    void findById_NoWorkplaceById_ReturningNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/workplaces/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", ID)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Workplace", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql"})
    void create_SimpleValues_ReturningCreatedWorkplace() throws Exception {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest(UUID.fromString(ID),
                1, Workplace.Type.DESK, true, true);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        mockMvc.perform(post("/api/v1/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.floor").value(1),
                        jsonPath("$.type").value("DESK"),
                        jsonPath("$.computerPresent").value(true),
                        jsonPath("$.available").value(true)
                );
    }

    @Test
    void create_NoOffice_RetuningNotFound() throws Exception {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest(UUID.fromString(ID),
                1, Workplace.Type.DESK, true, true);

        mockMvc.perform(post("/api/v1/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Office", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql"})
    void update_SimpleValues_ReturningUpdatedWorkplace() throws Exception {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest(UUID.fromString(ID), UUID.fromString(ID),
                2, Workplace.Type.ROOM, false, false);

        mockMvc.perform(put("/api/v1/workplaces/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.floor").value(2),
                        jsonPath("$.type").value("ROOM"),
                        jsonPath("$.computerPresent").value(false),
                        jsonPath("$.available").value(false)
                );
    }

    @Test
    void update_NoWorkplaceToUpdate_ReturningNotFound() throws Exception {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest(UUID.fromString(ID), UUID.fromString(ID),
                2, Workplace.Type.ROOM, false, false);

        mockMvc.perform(put("/api/v1/workplaces/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Workplace", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/workplaces/" + ID)
                .param("officeId", ID)
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());
    }
}
