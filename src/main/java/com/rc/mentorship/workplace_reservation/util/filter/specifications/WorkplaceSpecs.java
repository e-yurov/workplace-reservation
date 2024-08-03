package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Office_;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.entity.Workplace_;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import jakarta.persistence.criteria.Expression;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class WorkplaceSpecs {
    public Specification<Workplace> build(Map<String, Filter> filterMap) {
        return Specification
                .where(filterByFloor(filterMap.get(Workplace_.FLOOR)))
                .and(filterByType(filterMap.get(Workplace_.TYPE)))
                .and(filterByComputerPresent(filterMap.get("computerPresent")))
                .and(filterByAvailable(filterMap.get("available")))
                .and(filterByOfficeId(filterMap.get(Workplace_.OFFICE + "Id")));
    }

    private Specification<Workplace> filterByFloor(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            int floor;
            try {
                floor = Integer.parseInt(filter.getValue());
            } catch (NumberFormatException ex) {
                throw new FiltrationParamsFormatException(Workplace_.floor.getName());
            }

            return GlobalSpecs.buildWithFilterType(builder, filter.getType(),
                    root.get(Workplace_.floor), floor, Workplace_.floor.getName());
        };
    }

    private Specification<Workplace> filterByType(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            Workplace.Type type;
            try {
                type = Workplace.Type.valueOf(
                        filter.getValue().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new FiltrationParamsFormatException(Workplace_.type.getName());
            }

            return builder.equal(root.get(Workplace_.TYPE), type);
        };
    }

    private Specification<Workplace> filterByComputerPresent(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            return builder.equal(root.get(Workplace_.isComputerPresent),
                    Boolean.parseBoolean(filter.getValue()));
        };
    }

    private Specification<Workplace> filterByAvailable(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            return builder.equal(root.get(Workplace_.isAvailable),
                    Boolean.parseBoolean(filter.getValue()));
        };
    }

    private Specification<Workplace> filterByOfficeId(Filter filter) {
        return (root, query, builder) -> {
            Expression<UUID> expr = root.get(Workplace_.office).get(Office_.id);
            return GlobalSpecs.buildByUuid(expr, builder, filter, Workplace_.OFFICE + "Id");
        };
    }
}
