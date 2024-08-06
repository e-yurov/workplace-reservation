package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.*;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class OfficeSpecs {
    public Specification<Office> build(Map<String, Filter> filterMap) {
        return Specification
                .where(filterByStartTime(filterMap.get(OfficeWorkTime_.START_TIME)))
                .and(filterByEndTime(filterMap.get(OfficeWorkTime_.END_TIME)))
                .and(filterByLocationId(filterMap.get(Office_.LOCATION + "Id")));
    }

    private Specification<Office> filterByStartTime(Filter filter) {
        return filterByTime(filter, OfficeWorkTime_.startTime);
    }

    private Specification<Office> filterByEndTime(Filter filter) {
        return filterByTime(filter, OfficeWorkTime_.endTime);
    }

    private Specification<Office> filterByLocationId(Filter filter) {
        return (root, query, builder) -> {
            Expression<UUID> expr = root.get(Office_.location).get(Location_.id);
            return GlobalSpecs.buildByUuid(expr, builder, filter, Office_.LOCATION + "Id");
        };
    }

    private Specification<Office> filterByTime(Filter filter,
                                               SingularAttribute<OfficeWorkTime, LocalTime> attribute) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            LocalTime time;
            try {
                time = LocalTime.parse(filter.getValue());
            } catch (DateTimeParseException ex) {
                throw new FiltrationParamsFormatException(attribute.getName());
            }

            Expression<LocalTime> expr = root.get(Office_.workTime).get(attribute);
            return GlobalSpecs.buildWithFilterType(builder, filter.getType(),
                    expr, time, attribute.getName());
        };
    }
}
