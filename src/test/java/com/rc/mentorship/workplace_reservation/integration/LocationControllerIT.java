package com.rc.mentorship.workplace_reservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LocationControllerIT {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @LocalServerPort
    private int port;
    private static final String ID = "00000000-0000-0000-0000-000000000000";
    private static final String NOT_FOUND_MSG = "%s with ID: %s not found!";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final String token;

    @Autowired
    public LocationControllerIT(MockMvc mockMvc,
                                ObjectMapper objectMapper,
                                JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.token = jwtService.generateToken("admin");
    }

    @Test
    @Sql({"/sql/delete_all.sql", "/sql/insert_admin.sql", "/sql/insert_location.sql"})
    void findAll() throws Exception {
        mockMvc.perform(get("/api/v1/locations")
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content").isArray(),
                        jsonPath("$.content.length()").value(1),
                        jsonPath("$.content[0].city").value("City"),
                        jsonPath("$.content[0].address").value("Address")
                );
    }

    @Test
    @Sql({"/sql/delete_all.sql", "/sql/insert_admin.sql", "/sql/insert_location.sql"})
    void findById() throws Exception {
        mockMvc.perform(get("/api/v1/locations/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.city").value("City"),
                        jsonPath("$.address").value("Address")
                );
    }

    @Test
    @Sql({"/sql/delete_all.sql", "/sql/insert_admin.sql"})
    void findById_throwingNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/locations/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Location", ID))
                );
    }

    @Test
    @Sql({"/sql/delete_all.sql", "/sql/insert_admin.sql"})
    void create() throws Exception {
        LocationCreateRequest request = new LocationCreateRequest("City", "Address");

        mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + token)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.city").value("City"),
                jsonPath("$.address").value("Address")
        );
    }

    @Test
    @Sql({"/sql/delete_all.sql", "/sql/insert_admin.sql", "/sql/insert_location.sql"})
    void update() throws Exception {
        LocationUpdateRequest request =
                new LocationUpdateRequest(UUID.fromString(ID), "New city", "New address");

        mockMvc.perform(put("/api/v1/locations/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + token)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.city").value("New city"),
                jsonPath("$.address").value("New address")
        );
    }

    @Test
    @Sql({"/sql/delete_all.sql", "/sql/insert_admin.sql"})
    void update_throwingNotFound() throws Exception {
        LocationUpdateRequest request =
                new LocationUpdateRequest(UUID.fromString(ID), "New city", "New address");

        mockMvc.perform(put("/api/v1/locations/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + token)
        ).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "Location", ID))

        );
    }

    @Test
    @Sql({"/sql/delete_all.sql", "/sql/insert_admin.sql", "/sql/insert_location.sql"})
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/locations/" + ID)
                .header(AUTHORIZATION, BEARER + token)
        ).andExpectAll(
                status().isOk()
        );
    }
}
