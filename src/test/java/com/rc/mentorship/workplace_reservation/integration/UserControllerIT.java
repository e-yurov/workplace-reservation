package com.rc.mentorship.workplace_reservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerIT extends IntegrationTest {
    @Autowired
    public UserControllerIT(MockMvc mockMvc,
                            ObjectMapper objectMapper,
                            JwtService jwtService) {
        super(mockMvc, objectMapper, jwtService);
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void findAll_NoRoleFilter_ReturningPageOfOneUser() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content").isArray(),
                        jsonPath("$.content.length()").value(2)
                );
    }

    @Test
    @Sql({"/sql/insert_users_filter.sql"})
    void findAll_HasRoleFilter_ReturningFilteredPageOfOneUser() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .header(AUTHORIZATION, BEARER + token)
                .param("role", "USER")
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.content").isArray(),
                jsonPath("$.content.length()").value(1),
                jsonPath("$.content[0].id").value(ID)
        );
    }

    @Test
    @Sql({"/sql/insert_user.sql"})
    void findById_HasUserById_ReturningUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.name").value("Name"),
                        jsonPath("$.email").value("Email"),
                        jsonPath("$.role").value("USER")
                );
    }

    @Test
    void findById_NoUserById_ReturningNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "User", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_user.sql"})
    void update_SimpleValues_ReturningUpdatedUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(UUID.fromString(ID),
                "New name", "New password", "New email", "MANAGER");

        mockMvc.perform(put("/api/v1/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.name").value("New name"),
                        jsonPath("$.email").value("New email"),
                        jsonPath("$.role").value("MANAGER")
                );
    }

    @Test
    void update_NoUserToUpdate_ReturningNotFound() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(UUID.fromString(ID),
                "New name", "New password", "New email", "MANAGER");

        mockMvc.perform(put("/api/v1/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(String.format(NOT_FOUND_MSG, "User", ID))
                );
    }

    @Test
    @Sql({"/sql/insert_user.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/" + ID)
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());
    }
}
