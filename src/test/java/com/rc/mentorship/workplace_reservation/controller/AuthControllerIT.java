package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.container.KeycloakPostgresContainerIT;
import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.KeycloakService;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.AbstractUserRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIT extends KeycloakPostgresContainerIT {
    private static final String URL = "/api/v1/auth";

    private static final String NAME = "Name";
    private static final String EMAIL = "Email@test.com";
    private static final String PASSWORD = "Password";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;

    @Autowired
    public AuthControllerIT(MockMvc mockMvc,
                            ObjectMapper objectMapper,
                            UserRepository userRepository,
                            UserMapper userMapper,
                            KeycloakService keycloakService) {
        super(mockMvc, objectMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakService = keycloakService;
    }

    @Test
    void register_SimpleValues_ReturningToken() throws Exception {
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, PASSWORD);

        MvcResult mvcResult = mockMvc.perform(post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();
        UserResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserResponse.class);
        Optional<User> actualInDb = userRepository.findByEmail(EMAIL);
        UserRepresentation keycloakUser = keycloakService.getKeycloakUserByEmail(EMAIL);

        assertThat(result).isNotNull()
                .extracting(UserResponse::getName, UserResponse::getEmail)
                .containsExactly(NAME, EMAIL);
        assertThat(actualInDb).isPresent();
        assertThat(userMapper.toDto(actualInDb.get())).isEqualTo(result);
        assertThat(keycloakUser).isNotNull()
                .extracting(AbstractUserRepresentation::getUsername, AbstractUserRepresentation::getEmail)
                .containsExactly(NAME.toLowerCase(), EMAIL.toLowerCase());
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void register_HasUserWithSuchEmail_ReturningBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, PASSWORD);

        mockMvc.perform(post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }
//
//    @Test
//    @Sql("/sql/insert_user.sql")
//    void login_ValidCredentials_ReturningToken() throws Exception {
//        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);
//
//        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                )
//                .andExpect(status().isOk())
//                .andReturn();
//        JwtResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class);
//
//        assertThat(result.getToken()).isNotEmpty();
//    }
//
//    @Test
//    void login_NoUserWithSuchEmail_ReturningUnauthorized() throws Exception {
//        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                )
//                .andExpectAll(status().isUnauthorized());
//    }
//
//    @Test
//    @Sql("/sql/insert_user.sql")
//    void login_WrongPassword_ReturningUnauthorized() throws Exception {
//        LoginRequest request = new LoginRequest(EMAIL, "123");
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                )
//                .andExpect(status().isUnauthorized());
//    }
}
