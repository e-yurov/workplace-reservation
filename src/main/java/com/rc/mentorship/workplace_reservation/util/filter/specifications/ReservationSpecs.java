package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.entity.Reservation_;
import com.rc.mentorship.workplace_reservation.entity.User_;
import com.rc.mentorship.workplace_reservation.entity.Workplace_;
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
                .where(filterByStartDateTime(filterMap.get(Reservation_.START_DATE_TIME)))
                .and(filterByEndDateTime(filterMap.get(Reservation_.END_DATE_TIME)))
                .and(filterByUserId(filterMap.get(Reservation_.USER + "Id")))
                .and(filterByWorkplaceId(filterMap.get(Reservation_.WORKPLACE + "Id")));
    }

    private Specification<Reservation> filterByStartDateTime(Filter filter) {
        return filterByDateTime(filter, Reservation_.startDateTime);
    }

    private Specification<Reservation> filterByEndDateTime(Filter filter) {
        return filterByDateTime(filter, Reservation_.endDateTime);
    }

    private Specification<Reservation> filterByUserId(Filter filter) {
        return (root, query, builder) -> {
            Expression<UUID> expr = root.get(Reservation_.user).get(User_.id);
            return GlobalSpecs.buildByUuid(expr, builder, filter, Reservation_.USER + "Id");
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
            SingularAttribute<Reservation, LocalDateTime> attribute
    ) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            LocalDateTime dateTime;
            try {
                dateTime = LocalDateTime.parse(filter.getValue());
            } catch (DateTimeParseException ex) {
                throw new FiltrationParamsFormatException(attribute.getName());
            }

            Expression<LocalDateTime> expr = root.get(attribute);
            return GlobalSpecs.buildWithFilterType(builder, filter.getType(),
                    expr, dateTime, attribute.getName());
        };
    }

}
