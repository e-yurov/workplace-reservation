package com.rc.mentorship.workplace_reservation.security.config.configurers.access;

@FunctionalInterface
public interface AccessGranter {
    boolean hasAccess();
}
