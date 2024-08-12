package com.rc.mentorship.workplace_reservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"/sql/delete_all.sql", "/sql/insert_admin.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class IntegrationTest {
    protected final MockMvc mockMvc;
    protected final ObjectMapper objectMapper;

    @LocalServerPort
    protected int port;

    protected final String token;
    protected static final String ID = "00000000-0000-0000-0000-000000000000";
    protected static final String NOT_FOUND_MSG = "%s with ID: %s not found!";
    protected static final String AUTHORIZATION = "Authorization";
    protected static final String BEARER = "Bearer ";

    @Autowired
    public IntegrationTest(MockMvc mockMvc,
                           ObjectMapper objectMapper,
                           JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.token = jwtService.generateToken("admin");
    }
}
