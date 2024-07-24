package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@UtilityClass
public class ReservationSpecs {
    public Specification<Reservation> filterByStartDateTime(Filter filter) {
        return filterByDateTime(filter, "startDateTime");
    }

    public Specification<Reservation> filterByEndDateTime(Filter filter) {
        return filterByDateTime(filter, "endDateTime");
    }

    public Specification<Reservation> filterByUserId(Filter filter) {
        return filterByUuid(filter, "user");
    }

    public Specification<Reservation> filterByWorkplaceId(Filter filter) {
        return filterByUuid(filter, "workplace");
    }

    Specification<Reservation> filterByDateTime(Filter filter, String attribute) {
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
            return GlobalSpecs.buildByFilterType(builder, filter.getType(),
                    expr, dateTime, attribute);
        };
    }

    Specification<Reservation> filterByUuid(Filter filter, String attribute) {
        return (root, query, builder) ->
                GlobalSpecs.buildByUuid(root, builder, filter, attribute);
    }
}
