package com.rc.mentorship.workplace_reservation.util.filter.converter;

import com.rc.mentorship.workplace_reservation.util.filter.Filter;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@FunctionalInterface
public interface FilterToPredicateConverter<T> {
    Predicate<T> convert(Map<String, Filter> filters);

    default Predicate<T> applyAllPredicates(List<Predicate<T>> predicates) {
        Predicate<T> result = t -> true;
        for (var predicate: predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
