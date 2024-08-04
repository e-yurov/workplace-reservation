package com.rc.mentorship.workplace_reservation.security.config.util;

import com.rc.mentorship.workplace_reservation.security.config.configurers.Access;
import com.rc.mentorship.workplace_reservation.security.config.configurers.MatchingEntry;
import com.rc.mentorship.workplace_reservation.security.context.SecurityContextHolder;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class RequestMatcher {
    private List<MatchingEntry> matchers = new ArrayList<>();

    public boolean match(HttpMethod method, String uri) {
        Access access = identifyAccess();
        for (MatchingEntry matcher : matchers) {

        }

        return false;
    }

    public Access identifyAccess() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return Access.ALL;
        }

        return Access.AUTHENTICATED;
    }
}
