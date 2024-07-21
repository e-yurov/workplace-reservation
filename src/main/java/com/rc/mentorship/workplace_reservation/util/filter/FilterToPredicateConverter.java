package com.rc.mentorship.workplace_reservation.util.filter;

import java.util.function.Predicate;

public interface FilterToPredicateConverter<T> {
    Predicate<T> convert(Filter filter);
}
