package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LocationControllerIT extends IntegrationTest {
    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String CITY = "City";
    private static final String ADDRESS = "Address";
    private static final String NEW_CITY = "New city";
    private static final String NEW_ADDRESS = "New address";

    private final LocationResponse expected = new LocationResponse(ID, CITY, ADDRESS);

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public LocationControllerIT(MockMvc mockMvc,
                                ObjectMapper objectMapper,
                                JwtService jwtService,
                                LocationRepository locationRepository,
                                LocationMapper locationMapper) {
        super(mockMvc, objectMapper, jwtService);
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void findAll_NoCityFilter_ReturningPageOfOneLocation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/locations")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk()).andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        LocationResponse[] result = objectMapper.treeToValue(contentNode, LocationResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void findById_HasLocationById_ReturningLocation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/locations/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andReturn();
        LocationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LocationResponse.class);

        assertThat(result).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById_NoLocationById_ReturningNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/locations/" + ID)
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_SimpleValues_ReturningCreatedLocation() throws Exception {
        LocationCreateRequest request = new LocationCreateRequest(CITY, ADDRESS);

        MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + token)
        )
                .andExpect(status().isCreated())
                .andReturn();
        LocationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LocationResponse.class);
        Optional<Location> actualInDB = locationRepository.findById(result.getId());

        assertThat(result).extracting(LocationResponse::getCity, LocationResponse::getAddress)
                .containsExactly(CITY, ADDRESS);
        assertThat(actualInDB).isPresent();
        assertThat(locationMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void update_SimpleValues_ReturningUpdatedLocation() throws Exception {
        LocationUpdateRequest request =
                new LocationUpdateRequest(ID, NEW_CITY, NEW_ADDRESS);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/locations/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk()).andReturn();
        LocationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LocationResponse.class);
        Optional<Location> actualInDB = locationRepository.findById(result.getId());

        assertThat(result).extracting(LocationResponse::getId, LocationResponse::getCity, LocationResponse::getAddress)
                .containsExactly(ID, NEW_CITY, NEW_ADDRESS);
        assertThat(actualInDB).isPresent();
        assertThat(locationMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void update_NoLocationToUpdate_ReturningNotFound() throws Exception {
        LocationUpdateRequest request =
                new LocationUpdateRequest(ID, NEW_CITY, NEW_ADDRESS);

        mockMvc.perform(put("/api/v1/locations/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isNotFound());

        assertThat(locationRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/locations/" + ID)
                .header(AUTHORIZATION, BEARER + token)
        ).andExpect(status().isOk());

        assertThat(locationRepository.findById(ID)).isEmpty();
    }
}
