package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.container.KeycloakPostgresContainerIT;
import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import com.rc.mentorship.workplace_reservation.service.KeycloakService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WithMockUser(roles = {"USER", "ADMIN"})
public class UserControllerIT extends KeycloakPostgresContainerIT {
    private static final String URL = "/api/v1/users";

    private static final String NAME = "user";
    private static final String EMAIL = "user@test.com";
    private static final String ROLE = "USER";
    private static final String PASSWORD = "password";

    private static final String NEW_NAME = "newname";
    private static final String NEW_EMAIL = "newemail@test.com";
    private static final String NEW_ROLE = "ADMIN";

    private final UserResponse expected = new UserResponse(ID, NAME, EMAIL, Collections.singletonList(ROLE));

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;

    private final String token;

    @Autowired
    public UserControllerIT(MockMvc mockMvc,
                            ObjectMapper objectMapper,
                            UserRepository userRepository,
                            UserMapper userMapper,
                            KeycloakService keycloakService) {
        super(mockMvc, objectMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakService = keycloakService;
        this.token = super.getAdminToken();
    }

//    @BeforeEach
//    void beforeEach() {
//        keycloakService.addUser(new RegisterRequest(NAME, EMAIL, "password"));
//        keycloakService.getKeycloakIdByEmail(EMAIL);
//    }
//
//    @AfterEach
//    void afterEach() {
//        keycloakService.deleteUserByEmail(EMAIL);
//    }

    @Test
    @Sql("/sql/insert_user_tofind.sql")
    void findAll_NoRoleFilter_ReturningPageOfOneUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        UserResponse[] result = objectMapper.treeToValue(contentNode, UserResponse[].class);

//        assertThat(result).singleElement().isEqualTo(expected);
        assertThat(result).singleElement()
                .extracting(UserResponse::getId, UserResponse::getName, UserResponse::getEmail)
                .containsExactly(ID, "tofind", "tofind@test.com");
        assertThat(result[0].getRoles()).contains(ROLE);
    }

//    @Test
//    @Sql({"/sql/insert_users_filter.sql"})
//    void findAll_HasRoleFilter_ReturningFilteredPageOfOneUser() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(get(URL)
//                        .param("role", "USER")
//                )
//                .andExpect(status().isOk())
//                .andReturn();
//        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
//        UserResponse[] result = objectMapper.treeToValue(contentNode, UserResponse[].class);
//
//        assertThat(result).singleElement()
//                .extracting(UserResponse::getId, UserResponse::getName, UserResponse::getEmail)
//                .containsExactly(ID, "tofind", "tofind@test.com");
//        assertThat(result[0].getRoles()).contains(ROLE);
//    }

    @Test
    @Sql({"/sql/insert_user_tofind.sql"})
    void findById_HasUserById_ReturningUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + '/' + ID))
                .andExpect(status().isOk())
                .andReturn();
        UserResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UserResponse.class);

//        assertThat(result).isNotNull().isEqualTo(expected);
        assertThat(result).isNotNull()
                .extracting(UserResponse::getId, UserResponse::getName, UserResponse::getEmail)
                .containsExactly(ID, "tofind", "tofind@test.com");
        assertThat(result.getRoles()).contains(ROLE);
    }

    @Test
    void findById_NoUserById_ReturningNotFound() throws Exception {
        mockMvc.perform(get(URL + '/' + ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/sql/insert_user_toupdate.sql"})
    void update_SimpleValues_ReturningUpdatedUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(ID, NEW_NAME, NEW_EMAIL);

        MvcResult mvcResult = mockMvc.perform(put(URL + '/' + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();
        UserResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UserResponse.class);
        Optional<User> actualInDB = userRepository.findById(result.getId());

//        assertThat(result)
//                .extracting(UserResponse::getId, UserResponse::getName, UserResponse::getEmail)
//                .containsExactly(ID, NEW_NAME, NEW_EMAIL);
        assertThat(result.getRoles()).contains(ROLE);//
        assertThat(actualInDB).isPresent();
//        assertThat(userMapper.toDto(actualInDB.get())).isEqualTo(result);
        UserResponse dbResponse = userMapper.toDto(actualInDB.get());
        assertThat(result)
                .extracting(UserResponse::getId, UserResponse::getName, UserResponse::getEmail)
                .containsExactly(ID, NEW_NAME, NEW_EMAIL)
                .containsExactly(dbResponse.getId(), dbResponse.getName(), dbResponse.getEmail());
    }

    @Test
    void update_NoUserToUpdate_ReturningNotFound() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(ID, NEW_NAME, NEW_EMAIL);

        mockMvc.perform(put(URL + '/' + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        assertThat(userRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_user_todelete.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + '/' + ID))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(ID)).isEmpty();
    }
}
