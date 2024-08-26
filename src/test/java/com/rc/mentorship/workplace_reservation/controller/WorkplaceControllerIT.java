package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.container.BasePostgresContainerIT;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.WorkplaceUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.dto.response.WorkplaceResponse;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.mapper.WorkplaceMapper;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = {"USER", "ADMIN"})
public class WorkplaceControllerIT extends BasePostgresContainerIT {
    private static final String URL = "/api/v1/workplaces";
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
                                 WorkplaceRepository workplaceRepository,
                                 WorkplaceMapper workplaceMapper) {
        super(mockMvc, objectMapper);
        this.workplaceRepository = workplaceRepository;
        this.workplaceMapper = workplaceMapper;
    }

    @Test
    @Sql({"/sql/insert_workplace.sql"})
    void findAll_NoFilters_ReturningPageOfOneWorkplace() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL)
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        WorkplaceResponse[] result = objectMapper.treeToValue(contentNode, WorkplaceResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    @Sql({"/sql/insert_workplaces_filter.sql"})
    void findAll_HasFilters_ReturningFilteredPageOfOneWorkplace() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL)
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
        mockMvc.perform(get(URL)
                .param("officeId", OFFICE_ID_PARAM)
                .param("floor", "aa/invalid")
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.message").value(StringEndsWith.endsWith("'floor'!"))
        );
    }

    @Test
    @Sql({"/sql/insert_workplace.sql"})
    void findById_HasWorkplaceById_ReturningWorkplace() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + '/' + ID)
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
        mockMvc.perform(get(URL + '/' + ID)
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/sql/insert_office.sql"})
    void create_SimpleValues_ReturningCreatedWorkplace() throws Exception {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest(ID, FLOOR, TYPE, COMPUTER_PRESENT, AVAILABLE);

        MvcResult mvcResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isCreated())
                .andReturn();
        WorkplaceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                WorkplaceResponse.class);
        Optional<Workplace> actualInDB = workplaceRepository.findById(result.getId());

        assertThat(result).isNotNull()
                .extracting(WorkplaceResponse::getOfficeResponse, WorkplaceResponse::getFloor, WorkplaceResponse::getType,
                        WorkplaceResponse::isComputerPresent, WorkplaceResponse::isAvailable)
                .containsExactly(expectedOffice, FLOOR, TYPE, COMPUTER_PRESENT, AVAILABLE);
        assertThat(actualInDB).isPresent();
        assertThat(workplaceMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void create_NoOffice_RetuningNotFound() throws Exception {
        WorkplaceCreateRequest request = new WorkplaceCreateRequest(ID, FLOOR, TYPE, COMPUTER_PRESENT, AVAILABLE);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isNotFound());

        assertThat(workplaceRepository.findAll()).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_workplace.sql"})
    void update_SimpleValues_ReturningUpdatedWorkplace() throws Exception {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest(ID, ID,
                NEW_FLOOR, NEW_TYPE, NEW_COMPUTER_PRESENT, NEW_AVAILABLE);

        MvcResult mvcResult = mockMvc.perform(put(URL + '/' + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isOk())
                .andReturn();
        WorkplaceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                WorkplaceResponse.class);
        Optional<Workplace> actualInDB = workplaceRepository.findById(result.getId());

        assertThat(result).isNotNull()
                .extracting(WorkplaceResponse::getId, WorkplaceResponse::getFloor, WorkplaceResponse::getType,
                        WorkplaceResponse::isComputerPresent, WorkplaceResponse::isAvailable,
                        WorkplaceResponse::getOfficeResponse)
                .containsExactly(ID, NEW_FLOOR, NEW_TYPE, NEW_COMPUTER_PRESENT, NEW_AVAILABLE, expectedOffice);
        assertThat(actualInDB).isPresent();
        assertThat(workplaceMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void update_NoWorkplaceToUpdate_ReturningNotFound() throws Exception {
        WorkplaceUpdateRequest request = new WorkplaceUpdateRequest(ID, ID,
                NEW_FLOOR, NEW_TYPE, NEW_COMPUTER_PRESENT, NEW_AVAILABLE);

        mockMvc.perform(put(URL + '/' + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("officeId", OFFICE_ID_PARAM)
                )
                .andExpect(status().isNotFound());

        assertThat(workplaceRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_workplace.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + '/' + ID)
                .param("officeId", OFFICE_ID_PARAM)
        ).andExpect(status().isOk());

        assertThat(workplaceRepository.findById(ID)).isEmpty();
    }
}
