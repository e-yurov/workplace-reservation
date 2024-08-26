package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.container.BasePostgresContainerIT;
import com.rc.mentorship.workplace_reservation.dto.request.LocationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.LocationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.LocationResponse;
import com.rc.mentorship.workplace_reservation.entity.Location;
import com.rc.mentorship.workplace_reservation.mapper.LocationMapper;
import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = {"USER", "ADMIN"})
public class LocationControllerIT extends BasePostgresContainerIT {
    private static final String URL = "/api/v1/locations";

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
                                LocationRepository locationRepository,
                                LocationMapper locationMapper) {
        super(mockMvc, objectMapper);
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void findAll_NoCityFilter_ReturningPageOfOneLocation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        LocationResponse[] result = objectMapper.treeToValue(contentNode, LocationResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void findById_HasLocationById_ReturningLocation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + '/' + ID))
                .andExpect(status().isOk())
                .andReturn();
        LocationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                LocationResponse.class);

        assertThat(result).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById_NoLocationById_ReturningNotFound() throws Exception {
        mockMvc.perform(get(URL + '/' + ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_SimpleValues_ReturningCreatedLocation() throws Exception {
        LocationCreateRequest request = new LocationCreateRequest(CITY, ADDRESS);

        MvcResult mvcResult = mockMvc.perform(
                post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andReturn();
        LocationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                LocationResponse.class);
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

        MvcResult mvcResult = mockMvc.perform(put(URL + '/' + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andReturn();
        LocationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                LocationResponse.class);
        Optional<Location> actualInDB = locationRepository.findById(result.getId());

        assertThat(result)
                .extracting(LocationResponse::getId, LocationResponse::getCity, LocationResponse::getAddress)
                .containsExactly(ID, NEW_CITY, NEW_ADDRESS);
        assertThat(actualInDB).isPresent();
        assertThat(locationMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void update_NoLocationToUpdate_ReturningNotFound() throws Exception {
        LocationUpdateRequest request =
                new LocationUpdateRequest(ID, NEW_CITY, NEW_ADDRESS);

        mockMvc.perform(put(URL + '/' + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isNotFound());

        assertThat(locationRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_location.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + '/' + ID))
                .andExpect(status().isOk());

        assertThat(locationRepository.findById(ID)).isEmpty();
    }
}
