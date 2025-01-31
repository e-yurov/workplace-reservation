package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.apiclient.UserClient;
import com.rc.mentorship.workplace_reservation.dto.message.ReservationMessage;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.ReservationUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.ReservationResponse;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.OfficeWorkTime;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.entity.ReservationDateTime;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.BadReservationRequestException;
import com.rc.mentorship.workplace_reservation.exception.BadReservationTimeException;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.exception.WorkplaceNotAvailableException;
import com.rc.mentorship.workplace_reservation.mapper.ReservationMapper;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.KafkaProducerService;
import com.rc.mentorship.workplace_reservation.service.ReservationService;
import com.rc.mentorship.workplace_reservation.service.UserService;
import com.rc.mentorship.workplace_reservation.util.filter.FilterParamParser;
import com.rc.mentorship.workplace_reservation.util.filter.specifications.ReservationSpecs;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ReservationMapper reservationMapper;
    private final KafkaProducerService kafkaProducerService;
    private final UserService userService;
    private final MeterRegistry meterRegistry;
    private final UserClient userClient;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> findAllWithFilters(PageRequest pageRequest,
                                                        Map<String, String> filters) {
        Specification<Reservation> allSpecs = ReservationSpecs.build(
                FilterParamParser.parseAllParams(
                        filters, Set.of("pageNumber", "pageSize"))
        );
        return reservationRepository.findAll(allSpecs, pageRequest)
                .map(this::convertToResponseWithUser);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse findById(UUID id) {
        Reservation result = reservationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Reservation", id)
        );
        return convertToResponseWithUser(result);
    }

    @Override
    @Transactional
    @Timed(value = "reservation_creation_time")
    public ReservationResponse create(ReservationCreateRequest toCreate) {
        Reservation reservation = reservationMapper.toEntity(toCreate);
        fillReservationOrThrow(reservation,
                toCreate.getWorkplaceId(),
                reservation.getDateTime());
        reservationRepository.save(reservation);
        sendNotification(reservation);
        meterRegistry.counter("reservations_count")
                .increment(getDuration(reservation.getDateTime()));
        return convertToResponseWithUser(reservation);
    }

    @Override
    @Transactional
    public ReservationResponse update(ReservationUpdateRequest toUpdate) {
        reservationRepository.findById(toUpdate.getId()).orElseThrow(
                () -> new NotFoundException("Reservation", toUpdate.getId())
        );
        Reservation reservation = reservationMapper.toEntity(toUpdate);
        fillReservationOrThrow(reservation,
                toUpdate.getWorkplaceId(),
                reservation.getDateTime());
        reservationRepository.save(reservation);
        return convertToResponseWithUser(reservation);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        reservationRepository.deleteById(id);
    }

    private void fillReservationOrThrow(Reservation reservation,
                                        UUID workplaceId,
                                        ReservationDateTime dateTime) {
        if (dateTime.getStart().isAfter(dateTime.getEnd())) {
            throw new BadReservationTimeException("Time of start must be before time of end!");
        }

        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new NotFoundException("Workplace", workplaceId));
        checkOfficeWorkHours(workplace.getOffice().getWorkTime(), reservation.getDateTime());
        reservation.setWorkplace(workplace);
        checkAvailableAndNotReservedOrThrow(workplace.isAvailable(), workplaceId, reservation.getId(), dateTime);
    }

    private void checkOfficeWorkHours(OfficeWorkTime workTime, ReservationDateTime dateTime) {
        if (dateTime.getStart().toLocalTime().isBefore(workTime.getStartTime()) ||
                dateTime.getEnd().toLocalTime().isAfter(workTime.getEndTime())) {
            throw new BadReservationTimeException("Time must be within office work hours!");
        }
    }

    private void checkAvailableAndNotReservedOrThrow(boolean available, UUID workplaceId,
                                                     UUID id, ReservationDateTime dateTime) {
        if (!available) {
            throw new WorkplaceNotAvailableException(workplaceId);
        }
        if (!reservationRepository.checkReserved(workplaceId, id, dateTime.getStart(), dateTime.getEnd()).isEmpty()) {
            throw new BadReservationRequestException(workplaceId);
        }
    }

    private void sendNotification(Reservation reservation) {
        kafkaProducerService.sendMessage(
                new ReservationMessage(
                        reservation.getUser().getEmail(),
                        reservation.getDateTime().getStart(),
                        reservation.getDateTime().getEnd(),
                        reservation.getWorkplace().getId()
                )
        );
    }

    private long getDuration(ReservationDateTime dateTime) {
        Duration duration = Duration.between(dateTime.getStart(), dateTime.getEnd());
        return duration.toMinutes();
    }

    private ReservationResponse convertToResponseWithUser(Reservation reservation) {
        UserResponse userResponse = userService.getUserById(reservation.getUserId());
        ReservationResponse response = reservationMapper.toDto(reservation);
        response.setUserResponse(userResponse);
        return response;
    }
}
