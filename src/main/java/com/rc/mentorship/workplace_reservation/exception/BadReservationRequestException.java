package com.rc.mentorship.workplace_reservation.exception;

import java.time.LocalDateTime;
import java.util.UUID;

public class BadReservationRequestException extends RuntimeException {
    private static final String MESSAGE = "Workplace with ID: %s already reserved!";

    public BadReservationRequestException(UUID workplaceId) {
        super(String.format(MESSAGE, workplaceId));
    }
}
