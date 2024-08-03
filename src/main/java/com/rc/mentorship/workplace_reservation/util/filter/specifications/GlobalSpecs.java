package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import com.rc.mentorship.workplace_reservation.util.filter.FilterType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
class GlobalSpecs {
    <T extends Comparable<? super T>> Predicate buildWithFilterType(
            CriteriaBuilder builder,
            FilterType filterType,
            Expression<T> expr,
            T value,
            String attributeName
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
            default -> throw new FiltrationParamsFormatException(attributeName);
        }
    }

    Predicate buildByUuid(
            Expression<UUID> expr,
            CriteriaBuilder builder,
            Filter filter,
            String attributeName
    ) {
        if (filter == null) {
            return null;
        }

        UUID id;
        try {
            id = UUID.fromString(filter.getValue());
        } catch (IllegalArgumentException ex) {
            throw new FiltrationParamsFormatException(attributeName);
        }

        return builder.equal(expr, id);
    }
}
