package com.rc.mentorship.workplace_reservation.security.config.builders;

import com.rc.mentorship.workplace_reservation.security.config.configurers.RequestMatcherRegistry;

public class HttpSecurity {
    private RequestMatcherRegistry registry;

    public RequestMatcherRegistry authorizeRequests() {
        return this.registry;
    }
}
