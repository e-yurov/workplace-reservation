package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
class GlobalSpecs {
    <T extends Comparable<? super T>> Predicate buildByFilterType(
            CriteriaBuilder builder,
            FilterType filterType,
            Expression<T> expr,
            T value,
            String attribute
    ) {
        switch (filterType) {
            case GREATER_THAN -> {
                return builder.greaterThan(expr, value);
            }
            case GREATER_THAN_OR_EQUALS -> {
                return builder.greaterThanOrEqualTo(expr, value);
            }
            case LESS_THAN -> {
                return builder.lessThan(expr, value);
            }
            case LESS_THAN_OR_EQUALS -> {
                return builder.lessThanOrEqualTo(expr, value);
            }
            case EQUALS -> {
                return builder.equal(expr, value);
            }
            default -> throw new FiltrationParamsFormatException(attribute);
        }
    }

    <T> Predicate buildByUuid(
            Root<T> root,
            CriteriaBuilder builder,
            Filter filter,
            String attribute
    ) {
        if (filter == null) {
            return null;
        }

        UUID id;
        try {
            id = UUID.fromString(filter.getValue());
        } catch (IllegalArgumentException ex) {
            throw new FiltrationParamsFormatException(attribute + "Id");
        }

        return builder.equal(root.get(attribute)
                .get("id"), id);
    }
}
