package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.LoginRequest;
import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.JwtResponse;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.MessageDigest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIT
        extends IntegrationTest
{
    private static final String NAME = "Name";
    private static final String EMAIL = "Email@test.com";
    private static final String PASSWORD = "Password";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public AuthControllerIT(MockMvc mockMvc,
                            ObjectMapper objectMapper,
                            JwtService jwtService,
                            UserRepository userRepository,
                            UserMapper userMapper) {
        super(mockMvc, objectMapper, jwtService);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "admin",
            roles = {"USER", "ADMIN"}
    )
    void register_SimpleValues_ReturningToken() throws Exception {
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, PASSWORD);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();
        UserResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserResponse.class);
        Optional<User> actualInDb = userRepository.findByEmail(EMAIL);

        assertThat(result).isNotNull()
                .extracting(UserResponse::getName, UserResponse::getEmail)
                .containsExactly(NAME, EMAIL);
        assertThat(actualInDb).isPresent();
        assertThat(userMapper.toDto(actualInDb.get())).isEqualTo(result);
//        assertThat(result.getToken()).isNotEmpty();
//        assertThat(actualInDb).isPresent();
//        User user = actualInDb.get();
//        assertThat(user.getEmail()).isEqualTo(EMAIL);
//        assertThat(user.getName()).isEqualTo(NAME);
//        assertThat(user.getRole()).isEqualTo(User.Role.USER);
//        assertThat(user.getPassword()).asBase64Decoded().containsExactly(passwordDigest);
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void register_HasUserWithSuchEmail_ReturningBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, PASSWORD);

        mockMvc.perform(post("/api/v1/auth/register")
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
