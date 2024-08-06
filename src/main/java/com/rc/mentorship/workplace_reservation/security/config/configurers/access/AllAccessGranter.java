package com.rc.mentorship.workplace_reservation.security.config.configurers.access;

public class AllAccessGranter extends AbstractAccessGranter {
    AllAccessGranter(String name) {
        super(name);
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
