package com.rc.mentorship.workplace_reservation.security.config.configurers;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class RequestMatcherRegistry {
    private final List<MatchingEntry> matchers = new ArrayList<>();

    public RequestConfigurer requestMatchers(HttpMethod method, String... patterns) {
        return new RequestConfigurer(this, patterns, method);
    }

    public RequestConfigurer requestMatchers(String... patterns) {
        return requestMatchers(null, patterns);
    }

    public RequestConfigurer anyRequest() {
        return requestMatchers("/**");
    }

    void addMatcher(MatchingEntry matcher) {
        matchers.add(matcher);
    }
}
