package com.rc.mentorship.workplace_reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.container.BasePostgresContainerIT;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.*;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.service.JwtService;
import org.hamcrest.core.StringEndsWith;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = {"USER", "ADMIN"})
public class ReservationControllerIT extends BasePostgresContainerIT {
    private static final String URL = "/api/v1/reservations";

    private static final LocalDateTime START_DATE_TIME = LocalDateTime.parse("2024-07-18T12:00:00");
    private static final LocalDateTime NEW_START_DATE_TIME = START_DATE_TIME.plusHours(1);
    private static final LocalDateTime END_DATE_TIME = LocalDateTime.parse("2024-07-18T18:00:00");
    private static final LocalDateTime NEW_END_DATE_TIME = END_DATE_TIME.minusHours(1);

    private final LocationResponse expectedLocation = new LocationResponse(ID, "City", "Address");
    private final OfficeResponse expectedOffice = new OfficeResponse(ID, LocalTime.of(8, 0),
            LocalTime.of(18, 0), expectedLocation);
    private final WorkplaceResponse expectedWorkplace = new WorkplaceResponse(ID, 1, Workplace.Type.DESK,
            true, true, expectedOffice);
    //todo: check
    private final UserResponse expectedUser = new UserResponse(ID, "Name", "Email", null);
    private final ReservationResponse expected = new ReservationResponse(ID, START_DATE_TIME, END_DATE_TIME,
            expectedUser, expectedWorkplace);

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationControllerIT(MockMvc mockMvc,
                                   ObjectMapper objectMapper,
                                   ReservationRepository reservationRepository,
                                   ReservationMapper reservationMapper) {
        super(mockMvc, objectMapper);
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @Test
    @Sql({"/sql/insert_reservation.sql"})
    void findAll_NoFilters_ReturningPageOfOneReservation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        ReservationResponse[] result = objectMapper.treeToValue(contentNode, ReservationResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    @Sql({"/sql/insert_reservations_filters.sql"})
    void findAll_HasFilters_ReturningFilteredPageOfOneReservation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL)
                        .param("startDateTime", "gt/2024-07-18T11:00:00")
                        .param("endDateTime", "lte/2024-07-18T18:00:00")
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode contentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        ReservationResponse[] result = objectMapper.treeToValue(contentNode, ReservationResponse[].class);

        assertThat(result).singleElement().isEqualTo(expected);
    }

    @Test
    void findAll_WrongFiltersFormat_ReturningBadRequest() throws Exception {
        mockMvc.perform(get(URL)
                        .param("startDateTime", "invalid")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/sql/insert_reservation.sql"})
    void findById_HasReservationById_ReturningReservation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + '/' + ID))
                .andExpect(status().isOk())
                .andReturn();
        ReservationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ReservationResponse.class);

        assertThat(result).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById_NoReservationById_ReturningNotFound() throws Exception {
        mockMvc.perform(get(URL + '/' + ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/sql/insert_for_creating_reservation.sql"})
    void create_SimpleValues_ReturningCreatedReservation() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(ID, ID, START_DATE_TIME, END_DATE_TIME);

        MvcResult mvcResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andReturn();
        ReservationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ReservationResponse.class);
        Optional<Reservation> actualInDB = reservationRepository.findById(result.getId());

        assertThat(result).isNotNull()
                .extracting(ReservationResponse::getUserResponse, ReservationResponse::getWorkplaceResponse,
                        ReservationResponse::getStartDateTime, ReservationResponse::getEndDateTime)
                .containsExactly(expectedUser, expectedWorkplace, START_DATE_TIME, END_DATE_TIME);
        assertThat(actualInDB).isPresent();
        assertThat(reservationMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    @Sql({"/sql/insert_for_creating_reservation.sql"})
    void create_TimeOfStartBeforeTimeOfEnd_ReturningBadRequest() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(ID, ID, END_DATE_TIME, START_DATE_TIME);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringStartsWith.startsWith("Wrong reservation time!"))
                );

        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_for_creating_reservation_workplace_not_available.sql"})
    void create_WorkplaceNotAvailableForReservation_ReturningBadRequest() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(ID, ID, START_DATE_TIME, END_DATE_TIME);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringEndsWith.endsWith("is not available for reservation!"))
                );

        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_reservation.sql"})
    void create_AlreadyReserved_ReturningBadRequest() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(ID, ID, START_DATE_TIME, END_DATE_TIME);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringEndsWith.endsWith("already reserved!"))
                );
    }

    @Test
    void create_NoWorkplace_RetuningNotFound() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest(ID, ID, START_DATE_TIME, END_DATE_TIME);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringStartsWith.startsWith("Workplace"))
                );

        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_reservation.sql"})
    void update_SimpleValues_ReturningUpdatedReservation() throws Exception {
        ReservationUpdateRequest request = new ReservationUpdateRequest(ID, ID, ID,
                NEW_START_DATE_TIME, NEW_END_DATE_TIME);

        MvcResult mvcResult = mockMvc.perform(put(URL + '/' + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();
        ReservationResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ReservationResponse.class);
        Optional<Reservation> actualInDB = reservationRepository.findById(result.getId());

        assertThat(result).isNotNull()
                .extracting(ReservationResponse::getId, ReservationResponse::getUserResponse, ReservationResponse::getWorkplaceResponse,
                        ReservationResponse::getStartDateTime, ReservationResponse::getEndDateTime)
                .containsExactly(ID, expectedUser, expectedWorkplace, NEW_START_DATE_TIME, NEW_END_DATE_TIME);
        assertThat(actualInDB).isPresent();
        assertThat(reservationMapper.toDto(actualInDB.get())).isEqualTo(result);
    }

    @Test
    void update_NoReservationToUpdate_ReturningNotFound() throws Exception {
        ReservationUpdateRequest request = new ReservationUpdateRequest(ID, ID, ID,
                NEW_START_DATE_TIME, NEW_END_DATE_TIME);

        mockMvc.perform(put(URL + '/' + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        assertThat(reservationRepository.findById(ID)).isEmpty();
    }

    @Test
    @Sql({"/sql/insert_reservation_another.sql"})
    void update_AlreadyReservedForNewTime_ReturningBadRequest() throws Exception {
        ReservationUpdateRequest request = new ReservationUpdateRequest(ID, ID, ID,
                START_DATE_TIME.minusHours(2), END_DATE_TIME.minusHours(2));

        mockMvc.perform(put(URL + '/' + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value(StringEndsWith.endsWith("already reserved!"))
                );
    }

    @Test
    @Sql({"/sql/insert_reservation.sql"})
    void delete_SimpleValues_ReturningOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + '/' + ID))
                .andExpect(status().isOk());

        assertThat(reservationRepository.findById(ID)).isEmpty();
    }
}
