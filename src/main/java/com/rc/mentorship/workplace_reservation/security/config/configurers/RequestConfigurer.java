package com.rc.mentorship.workplace_reservation.security.config.configurers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;

@AllArgsConstructor
public class RequestConfigurer {
    private RequestMatcherRegistry registry;
    private String[] patterns;
    private HttpMethod method;

    public void permitAll() {
        for (String pattern: patterns) {
            this.registry.addMatcher(new MatchingEntry(pattern, method, Access.ALL));
        }
    }

    public void authenticated() {
        for (String pattern: patterns) {
            this.registry.addMatcher(new MatchingEntry(pattern, method, Access.AUTHENTICATED));
        }
    }

    public void denyAll() {
        for (String pattern: patterns) {
            this.registry.addMatcher(new MatchingEntry(pattern, method, Access.NO));
        }
    }

    public RequestMatcherRegistry getRegistry() {
        return registry;
    }
}
