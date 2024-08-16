package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends IntegrationTest {
    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String ROLE = "USER";

    private static final String NEW_NAME = "New name";
    private static final String NEW_EMAIL = "New email";
    private static final String NEW_ROLE = "MANAGER";

    private final UserResponse expected = new UserResponse(ID, NAME, EMAIL, ROLE);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserControllerIT(MockMvc mockMvc,
                            ObjectMapper objectMapper,
                            JwtService jwtService,
                            UserRepository userRepository,
                            UserMapper userMapper) {
        super(mockMvc, objectMapper, jwtService);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void findAll_NoRoleFilter_ReturningPageOfOneUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        UserResponse[] result = objectMapper.treeToValue(contentNode, UserResponse[].class);

        assertThat(result).hasSize(2).contains(expected);
    }

    @Test
    @Sql({"/sql/insert_users_filter.sql"})
    void findAll_HasRoleFilter_ReturningFilteredPageOfOneUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("role", "USER")
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        UserResponse[] result = objectMapper.treeToValue(contentNode, UserResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    @Sql({"/sql/insert_user.sql"})
    void findById_HasUserById_ReturningUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andReturn();
        UserResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UserResponse.class);

        assertThat(result).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById_NoUserById_ReturningNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/sql/insert_user.sql"})
    void update_SimpleValues_ReturningUpdatedUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(ID,
                NEW_NAME, "New password", NEW_EMAIL, NEW_ROLE);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isOk())
                .andReturn();
        UserResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UserResponse.class);
        Optional<User> actualInDB = userRepository.findById(result.getId());

        assertThat(result)
                .extracting(UserResponse::getId, UserResponse::getName,
                        UserResponse::getEmail, UserResponse::getRole)
                .containsExactly(ID, NEW_NAME, NEW_EMAIL, NEW_ROLE);
        assertThat(actualInDB).isPresent();
        assertThat(userMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void update_NoUserToUpdate_ReturningNotFound() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(ID,
                NEW_NAME, "New password", NEW_EMAIL, NEW_ROLE);

        mockMvc.perform(put("/api/v1/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isNotFound());

        assertThat(userRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_user.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/" + ID)
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());

        assertThat(userRepository.findById(ID)).isEmpty();
    }
}
