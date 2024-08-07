package com.rc.mentorship.workplace_reservation.security.role;

import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.security.context.SecurityContextHolder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleChecker {
    boolean check(Role[] permit) {
        if (!SecurityContextHolder.getContext().hasUser()) {
            return false;
        }
        if (permit.length == 0) {
            return true;
        }

        User user = SecurityContextHolder.getContext().getAuthentication().getUser();
        for (Role role : permit) {
            if (role.toString().equals(user.getRole())) {
                return true;
            }
        }
        return false;
    }
}
