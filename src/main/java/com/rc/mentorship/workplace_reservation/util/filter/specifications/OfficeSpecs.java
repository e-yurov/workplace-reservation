package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@UtilityClass
public class OfficeSpecs {
    public Specification<Office> filterByStartTime(Filter filter) {
        return filterByTime(filter, "startTime");
    }

    public Specification<Office> filterByEndTime(Filter filter) {
        return filterByTime(filter, "endTime");
    }

    public Specification<Office> filterByLocationId(Filter filter) {
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

    Specification<Office> filterByTime(Filter filter, String attribute) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            LocalTime time;
            try {
                time = LocalTime.parse(filter.getValue());
            } catch (DateTimeParseException ex) {
                throw new FiltrationParamsFormatException(attribute);
            }

            Expression<LocalTime> expr = root.get(attribute);
            return GlobalSpecs.buildByFilterType(builder, filter.getType(),
                    expr, time, attribute);
        };
    }
}
