package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.OfficeUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.dto.response.OfficeResponse;
import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.mapper.OfficeMapper;
import com.rc.mentorship.workplace_reservation.repository.OfficeRepository;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfficeControllerIT extends IntegrationTest {
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(18, 0);

    private static final LocalTime NEW_START_TIME = START_TIME.minusHours(1);
    private static final LocalTime NEW_END_TIME = END_TIME.minusHours(1);

    private final LocationResponse expectedLocation = new LocationResponse(ID, "City", "Address");
    private final OfficeResponse expected = new OfficeResponse(ID, START_TIME, END_TIME, expectedLocation);

    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;

    @Autowired
    public OfficeControllerIT(MockMvc mockMvc,
                              ObjectMapper objectMapper,
                              JwtService jwtService,
                              OfficeRepository officeRepository,
                              OfficeMapper officeMapper) {
        super(mockMvc, objectMapper, jwtService);
        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;
    }

    @Test
    @Sql({"/sql/insert_office.sql"})
    void findAll_NoFilters_ReturningPageOfOneOffice() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/offices")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        OfficeResponse[] result = objectMapper.treeToValue(contentNode, OfficeResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    @Sql({"/sql/insert_offices_filter.sql"})
    void findAll_HasFilters_ReturningFilteredPageOfOneOffice() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/offices")
                        .header(AUTHORIZATION, BEARER + token)
                        .param("locationId", ID.toString())
                        .param("startTime", "gte/08:00:00")
                        .param("endTime", "lt/19:00:00")
        ).andExpect(status().isOk()).andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        OfficeResponse[] result = objectMapper.treeToValue(contentNode, OfficeResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    void findAll_WrongFiltersFormat_ReturningBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/offices")
                .header(AUTHORIZATION, BEARER + token)
                .param("startTime", "invalid")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/sql/insert_office.sql"})
    void findById_HasOfficeById_ReturningOffice() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/offices/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andReturn();
        OfficeResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                OfficeResponse.class);

        assertThat(result).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById_NoOfficeById_ReturningNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/offices/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void create_SimpleValues_ReturningCreatedOffice() throws Exception {
        OfficeCreateRequest request =
                new OfficeCreateRequest(ID, START_TIME, END_TIME);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
        )
                .andExpect(status().isCreated())
                .andReturn();
        OfficeResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                OfficeResponse.class);
        Optional<Office> actualInDB = officeRepository.findById(result.getId());

        assertThat(result).isNotNull()
                .extracting(OfficeResponse::getLocationResponse, OfficeResponse::getStartTime, OfficeResponse::getEndTime)
                .containsExactly(expectedLocation, START_TIME, END_TIME);
        assertThat(actualInDB).isPresent();
        assertThat(officeMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void create_NoLocation_RetuningNotFound() throws Exception {
        OfficeCreateRequest request =
                new OfficeCreateRequest(ID, START_TIME, END_TIME);

        mockMvc.perform(post("/api/v1/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isNotFound());

        assertThat(officeRepository.findAll()).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_office.sql"})
    void update_SimpleValues_ReturningUpdatedOffice() throws Exception {
        OfficeUpdateRequest request = new OfficeUpdateRequest(ID, ID, NEW_START_TIME, NEW_END_TIME);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/offices/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isOk())
                .andReturn();
        OfficeResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                OfficeResponse.class);
        Optional<Office> actualInDB = officeRepository.findById(result.getId());

        assertThat(result)
                .extracting(OfficeResponse::getId, OfficeResponse::getStartTime,
                        OfficeResponse::getEndTime, OfficeResponse::getLocationResponse)
                .containsExactly(ID, NEW_START_TIME, NEW_END_TIME, expectedLocation);
        assertThat(actualInDB).isPresent();
        assertThat(officeMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void update_NoOfficeToUpdate_ReturningNotFound() throws Exception {
        OfficeUpdateRequest request = new OfficeUpdateRequest(ID, ID, NEW_START_TIME, NEW_END_TIME);

        mockMvc.perform(put("/api/v1/offices/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(AUTHORIZATION, BEARER + token)
                )
                .andExpect(status().isNotFound());

        assertThat(officeRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_office.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/offices/" + ID)
                        .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());

        assertThat(officeRepository.findById(ID)).isEmpty();
    }
}
