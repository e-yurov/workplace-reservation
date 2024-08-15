package com.rc.mentorship.workplace_reservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.LoginRequest;
import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.security.role.Role;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.security.MessageDigest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerIT extends IntegrationTest {
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
        RegisterRequest request = new RegisterRequest("Name", "Email", "Password");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.token").isNotEmpty(),
                        jsonPath("$.token").isString()
                );
        byte[] passwordDigest = messageDigest.digest("Password".getBytes());
        Optional<User> userOptional = userRepository.findByEmail("Email");
        assertThat(userOptional).isPresent();
        User user = userOptional.get();
        assertThat(user.getEmail()).isEqualTo("Email");
        assertThat(user.getName()).isEqualTo("Name");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getPassword()).asBase64Decoded().containsExactly(passwordDigest);
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void register_HasUserWithSuchEmail_ReturningBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest("Name", "Email", "Password");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value("User with email: Email already registered!")
                );
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void login_ValidCredentials_ReturningToken() throws Exception {
        LoginRequest request = new LoginRequest("Email", "Password");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.token").isNotEmpty(),
                        jsonPath("$.token").isString()
                );
    }

    @Test
    void login_NoUserWithSuchEmail_ReturningUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("Email", "Password");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value("Wrong email or password!")
                );
    }

    @Test
    @Sql("/sql/insert_user.sql")
    void login_WrongPassword_ReturningUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("Email", "123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value("Wrong email or password!")
                );
    }
}
