package com.rc.mentorship.workplace_reservation.security.config.configurers;

@FunctionalInterface
public interface AccessGranter {
    boolean hasAccess();
}
