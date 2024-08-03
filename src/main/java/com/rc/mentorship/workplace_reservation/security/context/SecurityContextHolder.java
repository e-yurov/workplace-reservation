package com.rc.mentorship.workplace_reservation.security.context;

public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static SecurityContext getContext() {
        if (contextHolder.get() == null) {
            contextHolder.set(new SecurityContext());
        }
        return contextHolder.get();
    }

    public static void setContext(SecurityContext context) {
        contextHolder.set(context);
    }

}
