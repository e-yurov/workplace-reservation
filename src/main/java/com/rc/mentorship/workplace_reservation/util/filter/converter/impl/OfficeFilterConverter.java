package com.rc.mentorship.workplace_reservation.util.filter.converter.impl;

import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterType;
import com.rc.mentorship.workplace_reservation.util.filter.converter.FilterToPredicateConverter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class OfficeFilterConverter implements FilterToPredicateConverter<Office> {
    @Override
    public Predicate<Office> convert(Map<String, Filter> filters) {
        Filter startTimeFilter = filters.get("startTime");
        Filter endTimeFilter = filters.get("endTime");
        List<Predicate<Office>> predicates = new ArrayList<>();
        if (startTimeFilter != null) {
             LocalTime startTime = LocalTime.parse(startTimeFilter.getValue());
             predicates.add(filterStartTime(startTime, startTimeFilter.getType()));
        }
        if (endTimeFilter != null) {
            LocalTime endTime = LocalTime.parse(endTimeFilter.getValue());
            predicates.add(filterEndTime(endTime, endTimeFilter.getType()));
        }

        return applyAllPredicates(predicates);
    }

    private Predicate<Office> filterStartTime(LocalTime startTime, FilterType filterType) {
        switch (filterType) {
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

    private Predicate<Office> filterEndTime(LocalTime endTime, FilterType filterType) {
        switch (filterType) {
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
}
