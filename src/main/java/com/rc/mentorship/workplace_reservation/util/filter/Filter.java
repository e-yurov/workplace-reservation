package com.rc.mentorship.workplace_reservation.util.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Filter {
    private final String field;
    private final FilterType type;
    private final String value;
}
