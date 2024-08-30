package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.*;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class ReservationSpecs {
    public Specification<Reservation> build(Map<String, Filter> filterMap) {
        return Specification
                .where(filterByStartDateTime(filterMap.get(ReservationDateTime_.START + "DateTime")))
                .and(filterByEndDateTime(filterMap.get(ReservationDateTime_.END + "DateTime")))
                .and(filterByUserId(filterMap.get(Reservation_.USER_ID)))
                .and(filterByWorkplaceId(filterMap.get(Reservation_.WORKPLACE + "Id")));
    }

    private Specification<Reservation> filterByStartDateTime(Filter filter) {
        return filterByDateTime(filter, ReservationDateTime_.start);
    }

    private Specification<Reservation> filterByEndDateTime(Filter filter) {
        return filterByDateTime(filter, ReservationDateTime_.end);
    }

    private Specification<Reservation> filterByUserId(Filter filter) {
        return (root, query, builder) -> {
            Expression<UUID> expr = root.get(Reservation_.userId);
            return GlobalSpecs.buildByUuid(expr, builder, filter, Reservation_.USER_ID);
        };
    }

    private Specification<Reservation> filterByWorkplaceId(Filter filter) {
        return (root, query, builder) -> {
            Expression<UUID> expr = root.get(Reservation_.workplace).get(Workplace_.id);
            return GlobalSpecs.buildByUuid(expr, builder, filter, Reservation_.WORKPLACE + "Id");
        };
    }

    private Specification<Reservation> filterByDateTime(
            Filter filter,
            SingularAttribute<ReservationDateTime, LocalDateTime> attribute
    ) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            LocalDateTime dateTime;
            try {
                dateTime = LocalDateTime.parse(filter.getValue());
            } catch (DateTimeParseException ex) {
                throw new FiltrationParamsFormatException(attribute.getName() + "DateTime");
            }

            Expression<LocalDateTime> expr = root.get(Reservation_.dateTime).get(attribute);
            return GlobalSpecs.buildWithFilterType(builder, filter.getType(),
                    expr, dateTime, attribute.getName() + "DateTime");
        };
    }

}
