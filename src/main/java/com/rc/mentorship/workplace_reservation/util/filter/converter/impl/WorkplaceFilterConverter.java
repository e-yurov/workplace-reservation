package com.rc.mentorship.workplace_reservation.util.filter.converter.impl;

import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.converter.FilterToPredicateConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class WorkplaceFilterConverter implements FilterToPredicateConverter<Workplace> {
    @Override
    public Predicate<Workplace> convert(Map<String, Filter> filters) {
        List<Predicate<Workplace>> predicates = new ArrayList<>();
        predicates.add(filterByFloor(filters.get("floor")));
        predicates.add(filterByWorkplaceType(filters.get("type")));
        predicates.add(filterByComputerPresent(filters.get("computerPresent")));
        predicates.add(filterByAvailable(filters.get("available")));
        predicates.add(filterByOfficeId(filters.get("officeId")));

        predicates.removeIf(Objects::isNull);
        return applyAllPredicates(predicates);
    }

    private Predicate<Workplace> filterByFloor(Filter floorFilter) {
        if (floorFilter == null) {
            return null;
        }

        int floor;
        try {
            floor = Integer.parseInt(floorFilter.getValue());
        } catch (NumberFormatException ex) {
            throw new FiltrationParamsFormatException("floor");
        }

        switch (floorFilter.getType()) {
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

    private Predicate<Workplace> filterByWorkplaceType(Filter workplaceTypeFilter) {
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

    private Predicate<Workplace> filterByComputerPresent(Filter computerPresentFilter) {
        if (computerPresentFilter == null) {
            return null;
        }

        boolean computerPresent = Boolean.parseBoolean(
                    computerPresentFilter.getValue().toLowerCase(Locale.ROOT));

        return workplace -> workplace.isComputerPresent() == computerPresent;
    }

    private Predicate<Workplace> filterByAvailable(Filter availableFilter) {
        if (availableFilter == null) {
            return null;
        }

        boolean available = Boolean.parseBoolean(
                availableFilter.getValue().toLowerCase(Locale.ROOT));

        return workplace -> workplace.isAvailable() == available;
    }

    private Predicate<Workplace> filterByOfficeId(Filter officeIdFilter) {
        if (officeIdFilter == null) {
            return null;
        }

        return workplace -> workplace.getOffice().getId()
                .toString().equals(officeIdFilter.getValue());
    }
}
