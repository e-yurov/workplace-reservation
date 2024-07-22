package com.rc.mentorship.workplace_reservation.util.filter.converter.impl;

import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.converter.FilterToPredicateConverter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class OfficeFilterConverter implements FilterToPredicateConverter<Office> {
    @Override
    public Predicate<Office> convert(Map<String, Filter> filters) {
        List<Predicate<Office>> predicates = new ArrayList<>();
        predicates.add(filterByStartTime(filters.get("startTime")));
        predicates.add(filterByEndTime(filters.get("endTime")));
        predicates.add(filterByLocationId(filters.get("locationId")));

        predicates.removeIf(Objects::isNull);
        return applyAllPredicates(predicates);
    }

    private Predicate<Office> filterByStartTime(Filter startTimeFilter) {
        if (startTimeFilter == null) {
            return null;
        }

        LocalTime startTime;
        try {
            startTime = LocalTime.parse(startTimeFilter.getValue());
        } catch (DateTimeParseException ex) {
            throw new FiltrationParamsFormatException("startTime");
        }

        switch (startTimeFilter.getType()) {
            case GREATER_THAN -> {
                return office -> office.getStartTime().isAfter(startTime);
            }
            case GREATER_THAN_OR_EQUALS -> {
                return office -> office.getStartTime().isAfter(startTime) ||
                        office.getStartTime().equals(startTime);
            }
            case LESS_THAN -> {
                return office -> office.getStartTime().isBefore(startTime);
            }
            case LESS_THAN_OR_EQUALS -> {
                return office -> office.getStartTime().isBefore(startTime) ||
                        office.getStartTime().equals(startTime);
            }
            case EQUALS -> {
                return office -> office.getStartTime().equals(startTime);
            }
            default -> throw new FiltrationParamsFormatException("startTime");
        }
    }

    private Predicate<Office> filterByEndTime(Filter endTimeFilter) {
        if (endTimeFilter == null) {
            return null;
        }

        LocalTime endTime;
        try {
            endTime = LocalTime.parse(endTimeFilter.getValue());
        } catch (DateTimeParseException ex) {
            throw new FiltrationParamsFormatException("endTime");
        }

        switch (endTimeFilter.getType()) {
            case GREATER_THAN -> {
                return office -> office.getEndTime().isAfter(endTime);
            }
            case GREATER_THAN_OR_EQUALS -> {
                return office -> office.getEndTime().isAfter(endTime) ||
                        office.getEndTime().equals(endTime);
            }
            case LESS_THAN -> {
                return office -> office.getEndTime().isBefore(endTime);
            }
            case LESS_THAN_OR_EQUALS -> {
                return office -> office.getEndTime().isBefore(endTime) ||
                        office.getEndTime().equals(endTime);
            }
            case EQUALS -> {
                return office -> office.getEndTime().equals(endTime);
            }
            default -> throw new FiltrationParamsFormatException("endTime");
        }
    }

    private Predicate<Office> filterByLocationId (Filter locationFilter) {
        if (locationFilter == null) {
            return null;
        }

        return office -> office.getLocation().getId()
                .toString().equals(locationFilter.getValue());
    }
}
