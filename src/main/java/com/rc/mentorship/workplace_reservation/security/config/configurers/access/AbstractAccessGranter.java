package com.rc.mentorship.workplace_reservation.security.config.configurers.access;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public abstract class AbstractAccessGranter implements AccessGranter {
    private final String name;
}
