package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@UtilityClass
public class ReservationSpecs {
    public Specification<Reservation> build(Map<String, Filter> filterMap) {
        return Specification
                .where(filterByStartDateTime(filterMap.get("startDateTime")))
                .and(filterByEndDateTime(filterMap.get("endDateTime")))
                .and(filterByUserId(filterMap.get("userId")))
                .and(filterByWorkplaceId(filterMap.get("workplaceId")));
    }

    private Specification<Reservation> filterByStartDateTime(Filter filter) {
        return filterByDateTime(filter, "startDateTime");
    }

    private Specification<Reservation> filterByEndDateTime(Filter filter) {
        return filterByDateTime(filter, "endDateTime");
    }

    private Specification<Reservation> filterByUserId(Filter filter) {
        return filterByUuid(filter, "user");
    }

    private Specification<Reservation> filterByWorkplaceId(Filter filter) {
        return filterByUuid(filter, "workplace");
    }

    private Specification<Reservation> filterByDateTime(Filter filter, String attribute) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            LocalDateTime dateTime;
            try {
                dateTime = LocalDateTime.parse(filter.getValue());
            } catch (DateTimeParseException ex) {
                throw new FiltrationParamsFormatException(attribute);
            }

            Expression<LocalDateTime> expr = root.get(attribute);
            return GlobalSpecs.buildWithFilterType(builder, filter.getType(),
                    expr, dateTime, attribute);
        };
    }

    private Specification<Reservation> filterByUuid(Filter filter, String attribute) {
        return (root, query, builder) ->
                GlobalSpecs.buildByUuid(root, builder, filter, attribute);
    }
}
