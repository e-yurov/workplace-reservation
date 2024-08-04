package com.rc.mentorship.workplace_reservation.security.config.configurers;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class RequestMatcherRegistry {
    private List<MatchingEntry> matchers = new ArrayList<>();

    public RequestConfigurer requestMatchers(HttpMethod method, String... patterns) {
        return new RequestConfigurer(this, patterns, method);
    }

    public RequestConfigurer requestMatchers(String... patterns) {
        return requestMatchers(null, patterns);
    }

    void addMatcher(MatchingEntry matcher) {
        matchers.add(matcher);
    }
}
