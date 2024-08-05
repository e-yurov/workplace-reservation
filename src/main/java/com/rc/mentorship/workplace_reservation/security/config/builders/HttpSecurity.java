package com.rc.mentorship.workplace_reservation.security.config.builders;

import com.rc.mentorship.workplace_reservation.security.config.configurers.RequestMatcherRegistry;
import com.rc.mentorship.workplace_reservation.security.config.util.RequestMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpSecurity {
    private final RequestMatcherRegistry registry;

    public RequestMatcherRegistry authorizeRequests() {
        return this.registry;
    }

    public RequestMatcher build() {
        return new RequestMatcher(this.registry.getMatchers());
    }
}
