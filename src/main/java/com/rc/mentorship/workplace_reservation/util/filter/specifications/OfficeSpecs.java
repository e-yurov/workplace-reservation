package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class OfficeSpecs {
    public static Specification<Office> filterByStartTime(Filter filter) {
        return filterByTime(filter, "startTime");
    }

    public static Specification<Office> filterByEndTime(Filter filter) {
        return filterByTime(filter, "endTime");
    }

    public static Specification<Office> filterByTime(Filter filter, String attribute) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            LocalTime localTime;
            try {
                localTime = LocalTime.parse(filter.getValue());
            } catch (DateTimeParseException ex) {
                throw new FiltrationParamsFormatException(filter.getKey());
            }

            Expression<LocalTime> expr = root.get(attribute);
            switch (filter.getType()) {
                case GREATER_THAN -> {
                    return builder.greaterThan(expr, localTime);
                }
                case GREATER_THAN_OR_EQUALS -> {
                    return builder.greaterThanOrEqualTo(expr, localTime);
                }
                case LESS_THAN -> {
                    return builder.lessThan(expr, localTime);
                }
                case LESS_THAN_OR_EQUALS -> {
                    return builder.lessThanOrEqualTo(expr, localTime);
                }
                case EQUALS -> {
                    return builder.equal(expr, localTime);
                }
                default -> {
                    throw new FiltrationParamsFormatException(filter.getKey());
                }
            }
        };
    }

    public static Specification<Office> filterByLocationId(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            UUID locationId;
            try {
                locationId = UUID.fromString(filter.getValue());
            } catch (IllegalArgumentException ex) {
                throw new FiltrationParamsFormatException("locationId");
            }
            return builder.equal(root.get("location").get("id"), locationId);
        };
    }
}
