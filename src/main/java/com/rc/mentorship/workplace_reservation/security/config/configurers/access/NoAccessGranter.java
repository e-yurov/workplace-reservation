package com.rc.mentorship.workplace_reservation.security.config.configurers.access;

public class NoAccessGranter extends AbstractAccessGranter {
    NoAccessGranter(String name) {
        super(name);
    }

    @Override
    public boolean hasAccess() {
        return false;
    }
}
