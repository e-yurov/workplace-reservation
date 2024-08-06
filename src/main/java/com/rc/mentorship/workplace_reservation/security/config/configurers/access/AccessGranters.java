package com.rc.mentorship.workplace_reservation.security.config.configurers.access;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AccessGranters {
    public static final AccessGranter ALL = new AllAccessGranter("ALL");
    public static final AccessGranter AUTHENTICATED = new AuthenticatedAccessGranter("AUTHENTICATED");
    public static final AccessGranter NO = new NoAccessGranter("NO");
}
