package com.rc.mentorship.workplace_reservation.security.config.configurers;

import com.rc.mentorship.workplace_reservation.security.config.configurers.access.AccessGranters;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;

@AllArgsConstructor
public class RequestConfigurer {
    private RequestMatcherRegistry registry;
    private String[] patterns;
    private HttpMethod method;

    public RequestMatcherRegistry permitAll() {
        for (String pattern: patterns) {
            this.registry.addMatcher(new MatchingEntry(pattern, method, AccessGranters.ALL));
        }
        return this.registry;
    }

    public RequestMatcherRegistry authenticated() {
        for (String pattern: patterns) {
            this.registry.addMatcher(new MatchingEntry(pattern, method, AccessGranters.AUTHENTICATED));
        }
        return this.registry;
    }

    public RequestMatcherRegistry denyAll() {
        for (String pattern: patterns) {
            this.registry.addMatcher(new MatchingEntry(pattern, method, AccessGranters.NO));
        }
        return this.registry;
    }
}
