package com.rc.mentorship.workplace_reservation.exception;

import java.util.UUID;

public class WorkplaceNotAvailableException extends RuntimeException {
    private static final String MESSAGE = "Workplace with ID: %s is not available for reservation!";

    public WorkplaceNotAvailableException(UUID workplaceId) {
        super(String.format(MESSAGE, workplaceId));
    }
}
