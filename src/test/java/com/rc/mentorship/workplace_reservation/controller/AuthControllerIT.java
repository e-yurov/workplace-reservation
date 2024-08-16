package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.LoginRequest;
import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.JwtResponse;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.security.role.Role;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.MessageDigest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerIT extends IntegrationTest {
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";

    private final UserRepository userRepository;
    private final MessageDigest messageDigest;

    @Autowired
    public AuthControllerIT(MockMvc mockMvc,
                            ObjectMapper objectMapper,
                            JwtService jwtService,
                            UserRepository userRepository,
                            MessageDigest messageDigest) {
        super(mockMvc, objectMapper, jwtService);
        this.userRepository = userRepository;
        this.messageDigest = messageDigest;
    }

    @Test
    void register_SimpleValues_ReturningToken() throws Exception {
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, PASSWORD);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();
        JwtResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class);
        byte[] passwordDigest = messageDigest.digest(PASSWORD.getBytes());
        Optional<User> userOptional = userRepository.findByEmail(EMAIL);

        assertThat(result.getToken()).isNotEmpty();
        assertThat(userOptional).isPresent();
        User user = userOptional.get();
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getPassword()).asBase64Decoded().containsExactly(passwordDigest);
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

    @Test
    @Sql("/sql/insert_user.sql")
    void login_ValidCredentials_ReturningToken() throws Exception {
        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();
        JwtResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class);

        assertThat(result.getToken()).isNotEmpty();
    }

    @Test
    void login_NoUserWithSuchEmail_ReturningUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void login_WrongPassword_ReturningUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(EMAIL, "123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized());
    }
}
