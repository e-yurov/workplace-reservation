package com.rc.mentorship.workplace_reservation.util.filter.specifications;

import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import com.rc.mentorship.workplace_reservation.util.filter.Filter;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

@UtilityClass
public class WorkplaceSpecs {
    public Specification<Workplace> filterByFloor(Filter filter) {
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

    public Specification<Workplace> filterByType(Filter filter) {
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

    public Specification<Workplace> filterByComputerPresent(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            return builder.equal(root.get("isComputerPresent"),
                    Boolean.parseBoolean(filter.getValue()));
        };
    }

    public Specification<Workplace> filterByAvailable(Filter filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return null;
            }

            return builder.equal(root.get("isAvailable"),
                    Boolean.parseBoolean(filter.getValue()));
        };
    }

    public Specification<Workplace> filterByOfficeId(Filter filter) {
        return (root, query, builder) ->
                GlobalSpecs.buildByUuid(root, builder, filter, "office");
    }
}
