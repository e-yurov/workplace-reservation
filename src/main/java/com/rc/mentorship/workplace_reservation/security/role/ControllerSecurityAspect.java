package com.rc.mentorship.workplace_reservation.security.role;

import com.rc.mentorship.workplace_reservation.exception.AccessDeniedException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
//@Component
public class ControllerSecurityAspect {
    @Pointcut("within(com.rc.mentorship.workplace_reservation.controller.*)")
    private void isController() {}

    @Before("isController() && @annotation(hasRole)")
    private void checkRole(HasRole hasRole) {
        if (!RoleChecker.check(hasRole.value())) {
            throw new AccessDeniedException();
        }
    }
}
