package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Office;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@UtilityClass
public class OfficeSpecs {
    public Specification<Office> build(Map<String, Filter> filterMap) {
        return Specification
                .where(filterByStartTime(filterMap.get("startTime")))
                .and(filterByEndTime(filterMap.get("endTime")))
                .and(filterByLocationId(filterMap.get("locationId")));
    }

    private Specification<Office> filterByStartTime(Filter filter) {
        return filterByTime(filter, "startTime");
    }

    private Specification<Office> filterByEndTime(Filter filter) {
        return filterByTime(filter, "endTime");
    }

    private Specification<Office> filterByLocationId(Filter filter) {
        return (root, query, builder) ->
                GlobalSpecs.buildByUuid(root, builder, filter, "location");
    }

    private Specification<Office> filterByTime(Filter filter, String attribute) {
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
            return GlobalSpecs.buildWithFilterType(builder, filter.getType(),
                    expr, time, attribute);
        };
    }
}
