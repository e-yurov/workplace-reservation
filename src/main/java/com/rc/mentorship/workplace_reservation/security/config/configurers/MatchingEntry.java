package com.rc.mentorship.workplace_reservation.security.config.configurers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@AllArgsConstructor
public class MatchingEntry {
    private String pattern;
    private HttpMethod method;
    private Access access;
}
