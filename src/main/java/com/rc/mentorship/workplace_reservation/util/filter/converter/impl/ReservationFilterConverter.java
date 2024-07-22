package com.rc.mentorship.workplace_reservation.util.filter.converter.impl;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterType;
import com.rc.mentorship.workplace_reservation.util.filter.converter.FilterToPredicateConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class ReservationFilterConverter implements FilterToPredicateConverter<Reservation> {
    @Override
    public Predicate<Reservation> convert(Map<String, Filter> filters) {
        Filter startDateTimeFilter = filters.get("startDateTime");
        Filter endDateTimeFilter = filters.get("endDateTime");
        List<Predicate<Reservation>> predicates = new ArrayList<>();
        if (startDateTimeFilter != null) {
            LocalDateTime startDateTime =
                    LocalDateTime.parse(startDateTimeFilter.getValue());
            predicates.add(filterStartDateTime(startDateTime,
                    startDateTimeFilter.getType()));
        }
        if (endDateTimeFilter != null) {
            LocalDateTime endDateTime =
                    LocalDateTime.parse(endDateTimeFilter.getValue());
            predicates.add(filterEndDateTime(endDateTime,
                    endDateTimeFilter.getType()));
        }

        return applyAllPredicates(predicates);
    }

    private Predicate<Reservation> filterStartDateTime(LocalDateTime startDateTime,
                                                       FilterType filterType) {
        switch (filterType) {
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

    private Predicate<Reservation> filterEndDateTime(LocalDateTime endDateTime,
                                                     FilterType filterType) {
        switch (filterType) {
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
}
