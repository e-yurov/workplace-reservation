package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.mapper.WorkplaceMapper;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WorkplaceControllerIT extends IntegrationTest {
    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String OFFICE_ID_PARAM = ID.toString();

    private static final int FLOOR = 1;
    private static final Workplace.Type TYPE = Workplace.Type.DESK;
    private static final boolean COMPUTER_PRESENT = true;
    private static final boolean AVAILABLE = true;

    private static final int NEW_FLOOR = 2;
    private static final Workplace.Type NEW_TYPE = Workplace.Type.ROOM;
    private static final boolean NEW_COMPUTER_PRESENT = false;
    private static final boolean NEW_AVAILABLE = false;

    private final LocationResponse expectedLocation = new LocationResponse(ID, "City", "Address");
    private final OfficeResponse expectedOffice = new OfficeResponse(ID, LocalTime.of(8, 0),
            LocalTime.of(18, 0), expectedLocation);
    private final WorkplaceResponse expected = new WorkplaceResponse(ID, FLOOR, TYPE,
            COMPUTER_PRESENT, AVAILABLE, expectedOffice);

    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceMapper workplaceMapper;

    @Autowired
    public WorkplaceControllerIT(MockMvc mockMvc,
                                 ObjectMapper objectMapper,
                                 JwtService jwtService,
                                 WorkplaceRepository workplaceRepository,
                                 WorkplaceMapper workplaceMapper) {
        super(mockMvc, objectMapper, jwtService);
        this.workplaceRepository = workplaceRepository;
        this.workplaceMapper = workplaceMapper;
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql"})
    void findAll_NoFilters_ReturningPageOfOneWorkplace() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/workplaces")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        WorkplaceResponse[] result = objectMapper.treeToValue(contentNode, WorkplaceResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplaces_filter.sql"})
    void findAll_HasFilters_ReturningFilteredPageOfOneWorkplace() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/workplaces")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", OFFICE_ID_PARAM)
                        .param("floor", "lt/2")
                        .param("type", "DESK")
                        .param("computerPresent", "true")
                        .param("available", "true")
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        WorkplaceResponse[] result = objectMapper.treeToValue(contentNode, WorkplaceResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    void findAll_WrongFiltersFormat_ReturningBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/workplaces")
                .header(AUTHORIZATION, BEARER + token)
                .param("officeId", OFFICE_ID_PARAM)
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
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/workplaces/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isOk())
                .andReturn();
        WorkplaceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                WorkplaceResponse.class);

        assertThat(result).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById_NoWorkplaceById_ReturningNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/workplaces/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql"})
    void create_SimpleValues_ReturningCreatedWorkplace() throws Exception {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest(ID, FLOOR, TYPE, COMPUTER_PRESENT, AVAILABLE);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isCreated())
                .andReturn();
        WorkplaceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                WorkplaceResponse.class);
        Optional<Workplace> actualInDB = workplaceRepository.findById(result.getId());

        assertThat(result).isNotNull()
                .extracting(WorkplaceResponse::getFloor, WorkplaceResponse::getType,
                        WorkplaceResponse::isComputerPresent, WorkplaceResponse::isAvailable)
                .containsExactly(FLOOR, TYPE, COMPUTER_PRESENT, AVAILABLE);
        assertThat(actualInDB).isPresent();
        assertThat(workplaceMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void create_NoOffice_RetuningNotFound() throws Exception {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest(ID, FLOOR, TYPE, COMPUTER_PRESENT, AVAILABLE);

        mockMvc.perform(post("/api/v1/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql"})
    void update_SimpleValues_ReturningUpdatedWorkplace() throws Exception {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest(ID, ID,
                NEW_FLOOR, NEW_TYPE, NEW_COMPUTER_PRESENT, NEW_AVAILABLE);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/workplaces/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isOk())
                .andReturn();
        WorkplaceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                WorkplaceResponse.class);
        Optional<Workplace> actualInDB = workplaceRepository.findById(result.getId());

        assertThat(result).isNotNull()
                .extracting(WorkplaceResponse::getId, WorkplaceResponse::getFloor, WorkplaceResponse::getType,
                        WorkplaceResponse::isComputerPresent, WorkplaceResponse::isAvailable)
                .containsExactly(ID, NEW_FLOOR, NEW_TYPE, NEW_COMPUTER_PRESENT, NEW_AVAILABLE);
        assertThat(actualInDB).isPresent();
        assertThat(workplaceMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void update_NoWorkplaceToUpdate_ReturningNotFound() throws Exception {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest(ID, ID,
                NEW_FLOOR, NEW_TYPE, NEW_COMPUTER_PRESENT, NEW_AVAILABLE);

        mockMvc.perform(put("/api/v1/workplaces/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isNotFound());

        assertThat(workplaceRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_location.sql", "/sql/insert_office.sql", "/sql/insert_workplace.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/workplaces/" + ID)
                .param("officeId", OFFICE_ID_PARAM)
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());

        assertThat(workplaceRepository.findById(ID)).isEmpty();
    }
}
