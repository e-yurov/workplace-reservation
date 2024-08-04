package com.rc.mentorship.workplace_reservation.security.config.configurers;

import com.rc.mentorship.workplace_reservation.security.context.SecurityContext;
import com.rc.mentorship.workplace_reservation.security.context.SecurityContextHolder;

public class AccessGranters {
    public static final AccessGranter ALL = () -> true;
    public static final AccessGranter AUTHENTICATED = () -> {
        SecurityContext context = SecurityContextHolder.getContext();
        return context != null && context.hasUser();
    };
    public static final AccessGranter NO = () -> false;
}
