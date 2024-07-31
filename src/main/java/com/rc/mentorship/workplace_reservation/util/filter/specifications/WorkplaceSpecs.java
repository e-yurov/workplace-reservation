package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;
import java.util.Map;

@UtilityClass
public class WorkplaceSpecs {
    public Specification<Workplace> build(Map<String, Filter> filterMap) {
        return Specification
                .where(filterByFloor(filterMap.get("floor")))
                .and(filterByType(filterMap.get("type")))
                .and(filterByComputerPresent(filterMap.get("computerPresent")))
                .and(filterByAvailable(filterMap.get("available")))
                .and(filterByOfficeId(filterMap.get("officeId")));
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
                throw new FiltrationParamsFormatException("floor");
            }

            return GlobalSpecs.buildWithFilterType(builder, filter.getType(),
                    root.get("floor"), floor, "floor");
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
                throw new FiltrationParamsFormatException("type");
            }

            return builder.equal(root.get("type"), type);
        };
    }

    private Specification<Workplace> filterByComputerPresent(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            return builder.equal(root.get("isComputerPresent"),
                    Boolean.parseBoolean(filter.getValue()));
        };
    }

    private Specification<Workplace> filterByAvailable(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            return builder.equal(root.get("isAvailable"),
                    Boolean.parseBoolean(filter.getValue()));
        };
    }

    private Specification<Workplace> filterByOfficeId(Filter filter) {
        return (root, query, builder) ->
                GlobalSpecs.buildByUuid(root, builder, filter, "office");
    }
}
