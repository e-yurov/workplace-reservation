package com.rc.mentorship.workplace_reservation.util;

import com.rc.mentorship.workplace_reservation.dto.response.OfficeIdResponse;
import com.rc.mentorship.workplace_reservation.entity.Reservation;
import com.rc.mentorship.workplace_reservation.entity.Workplace;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.repository.ReservationRepository;
import com.rc.mentorship.workplace_reservation.repository.WorkplaceRepository;
import com.rc.mentorship.workplace_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ManagerPermissionEvaluator implements PermissionEvaluator {
    private final UserService userService;
    private final ReservationRepository reservationRepository;
    private final WorkplaceRepository workplaceRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        UUID officeId = (UUID) targetDomainObject;
        UUID keycloakId = getKeycloakId(authentication);
        OfficeIdResponse officeIdResponse = userService.getOfficeIdByKeycloakId(keycloakId);
        return officeIdResponse.getOfficeId().equals(officeId.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                 String targetType, Object permission) {
        UUID officeId;
        switch (targetType) {
            case "workplaceId" -> {
                UUID workplaceId = (UUID) targetId;
                Workplace workplace = workplaceRepository.findById(workplaceId).orElseThrow(
                        () -> new NotFoundException("Workplace", workplaceId)
                );
                officeId = workplace.getOffice().getId();
            }
            case "reservationId" -> {
                UUID reservationId = (UUID) targetId;
                Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                        () -> new NotFoundException("Reservation", reservationId)
                );
                officeId = reservation.getWorkplace().getOffice().getId();
            }
            case "officeId" -> {
                officeId = (UUID) targetId;
            }
            default -> {
                return false;
            }
        }

        return hasPermission(authentication, officeId, permission);
    }

    private UUID getKeycloakId(Authentication authentication) {
        Jwt credentials = (Jwt) authentication.getCredentials();
        return UUID.fromString(credentials.getSubject());
    }
}
