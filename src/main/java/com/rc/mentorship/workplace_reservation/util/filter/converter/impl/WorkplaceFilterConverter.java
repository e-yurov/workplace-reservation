package com.rc.mentorship.workplace_reservation.util.filter.converter.impl;

import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterType;
import com.rc.mentorship.workplace_reservation.util.filter.converter.FilterToPredicateConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class WorkplaceFilterConverter implements FilterToPredicateConverter<Workplace> {
    @Override
    public Predicate<Workplace> convert(Map<String, Filter> filters) {
        List<Predicate<Workplace>> predicates = new ArrayList<>();
        predicates.add(filterFloor(filters.get("floor")));
        predicates.add(filterWorkplaceType(filters.get("type")));
        predicates.add(filterComputerPresent(filters.get("computerPresent")));
        predicates.add(filterAvailable(filters.get("available")));
        predicates.removeIf(Objects::isNull);
        return applyAllPredicates(predicates);
    }

    private Predicate<Workplace> filterFloor(Filter floorFilter) {
        if (floorFilter == null) {
            return null;
        }

        int floor;
        try {
            floor = Integer.parseInt(floorFilter.getValue());
        } catch (NumberFormatException ex) {
            throw new FiltrationParamsFormatException("floor");
        }

        FilterType filterType = floorFilter.getType();
        switch (filterType) {
            case GREATER_THAN -> {
                return workplace -> workplace.getFloor() > floor;
            }
            case GREATER_THAN_OR_EQUALS -> {
                return workplace -> workplace.getFloor() >= floor;
            }
            case LESS_THAN -> {
                return workplace -> workplace.getFloor() < floor;
            }
            case LESS_THAN_OR_EQUALS -> {
                return workplace -> workplace.getFloor() <= floor;
            }
            case EQUALS -> {
                return workplace -> workplace.getFloor() == floor;
            }
            default -> throw new FiltrationParamsFormatException("floor");
        }
    }

    private Predicate<Workplace> filterWorkplaceType(Filter workplaceTypeFilter) {
        if (workplaceTypeFilter == null) {
            return null;
        }

        Workplace.Type workplaceType;
        try {
            workplaceType = Workplace.Type.valueOf(
                    workplaceTypeFilter.getValue().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new FiltrationParamsFormatException("type");
        }

        return workplace -> workplace.getType() == workplaceType;
    }

    private Predicate<Workplace> filterComputerPresent(Filter computerPresentFilter) {
        if (computerPresentFilter == null) {
            return null;
        }

        boolean computerPresent = Boolean.parseBoolean(
                    computerPresentFilter.getValue().toLowerCase(Locale.ROOT));

        return workplace -> workplace.isComputerPresent() == computerPresent;
    }

    private Predicate<Workplace> filterAvailable(Filter availableFilter) {
        if (availableFilter == null) {
            return null;
        }

        boolean available = Boolean.parseBoolean(
                availableFilter.getValue().toLowerCase(Locale.ROOT));

        return workplace -> workplace.isAvailable() == available;
    }
}
