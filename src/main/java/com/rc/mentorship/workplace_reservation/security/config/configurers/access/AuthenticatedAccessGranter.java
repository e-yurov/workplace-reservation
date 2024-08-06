package com.rc.mentorship.workplace_reservation.security.config.configurers.access;

import com.rc.mentorship.workplace_reservation.security.context.SecurityContext;
import com.rc.mentorship.workplace_reservation.security.context.SecurityContextHolder;

public class AuthenticatedAccessGranter extends AbstractAccessGranter {
    AuthenticatedAccessGranter(String name) {
        super(name);
    }

    @Override
    public boolean hasAccess() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context != null && context.hasUser();
    }
}
