package com.rc.mentorship.workplace_reservation.security.config.configurers;

import com.rc.mentorship.workplace_reservation.security.config.configurers.access.AccessGranter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@AllArgsConstructor
public class MatchingEntry {
    private String pattern;
    private HttpMethod method;
    private AccessGranter accessGranter;
}
