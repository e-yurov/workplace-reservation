package com.rc.mentorship.workplace_reservation.util.filter.converter.impl;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.converter.FilterToPredicateConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class ReservationFilterConverter implements FilterToPredicateConverter<Reservation> {
    @Override
    public Predicate<Reservation> convert(Map<String, Filter> filters) {
        List<Predicate<Reservation>> predicates = new ArrayList<>();
        predicates.add(filterByStartDateTime(filters.get("startDateTime")));
        predicates.add(filterByEndDateTime(filters.get("endDateTime")));
        predicates.add(filterByUserId(filters.get("userId")));
        predicates.add(filterByWorkplaceId(filters.get("workplaceId")));

        predicates.removeIf(Objects::isNull);
        return applyAllPredicates(predicates);
    }

    private Predicate<Reservation> filterByStartDateTime(Filter startDateTimeFilter) {
        if (startDateTimeFilter == null) {
            return null;
        }

        LocalDateTime startDateTime;
        try {
            startDateTime = LocalDateTime.parse(startDateTimeFilter.getValue());
        } catch (DateTimeParseException ex) {
            throw new FiltrationParamsFormatException("startDateTime");
        }

        switch (startDateTimeFilter.getType()) {
            case GREATER_THAN -> {
                return reservation -> reservation.getStartDateTime().isAfter(startDateTime);
            }
            case GREATER_THAN_OR_EQUALS -> {
                return reservation -> reservation.getStartDateTime().isAfter(startDateTime) ||
                        reservation.getStartDateTime().equals(startDateTime);
            }
            case LESS_THAN -> {
                return reservation -> reservation.getStartDateTime().isBefore(startDateTime);
            }
            case LESS_THAN_OR_EQUALS -> {
                return reservation -> reservation.getStartDateTime().isBefore(startDateTime) ||
                        reservation.getStartDateTime().equals(startDateTime);
            }
            case EQUALS -> {
                return reservation -> reservation.getStartDateTime().equals(startDateTime);
            }
            default -> throw new FiltrationParamsFormatException("startDateTime");
        }
    }

    private Predicate<Reservation> filterByEndDateTime(Filter endDateTimeFilter) {
        if (endDateTimeFilter == null) {
            return null;
        }

        LocalDateTime endDateTime;
        try {
            endDateTime = LocalDateTime.parse(endDateTimeFilter.getValue());
        } catch (DateTimeParseException ex) {
            throw new FiltrationParamsFormatException("endDateTime");
        }

        switch (endDateTimeFilter.getType()) {
            case GREATER_THAN -> {
                return reservation -> reservation.getEndDateTime().isAfter(endDateTime);
            }
            case GREATER_THAN_OR_EQUALS -> {
                return reservation -> reservation.getEndDateTime().isAfter(endDateTime) ||
                        reservation.getEndDateTime().equals(endDateTime);
            }
            case LESS_THAN -> {
                return reservation -> reservation.getEndDateTime().isBefore(endDateTime);
            }
            case LESS_THAN_OR_EQUALS -> {
                return reservation -> reservation.getEndDateTime().isBefore(endDateTime) ||
                        reservation.getEndDateTime().equals(endDateTime);
            }
            case EQUALS -> {
                return reservation -> reservation.getEndDateTime().equals(endDateTime);
            }
            default -> throw new FiltrationParamsFormatException("endDateTime");
        }
    }

    private Predicate<Reservation> filterByUserId(Filter userIdFilter) {
        if (userIdFilter == null) {
            return null;
        }

        return reservation -> reservation.getUser().getId()
                .toString().equals(userIdFilter.getValue());
    }

    private Predicate<Reservation> filterByWorkplaceId(Filter workplaceIdFilter) {
        if (workplaceIdFilter == null) {
            return null;
        }

        return reservation -> reservation.getWorkplace().getId()
                .toString().equals(workplaceIdFilter.getValue());
    }
}
