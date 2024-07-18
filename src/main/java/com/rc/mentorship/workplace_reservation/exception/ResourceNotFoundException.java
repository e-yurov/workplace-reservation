package com.rc.mentorship.workplace_reservation.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    private static final String MESSAGE = "%s with ID: %s not found!";

    public ResourceNotFoundException(String type, UUID id) {
        super(String.format(MESSAGE, type, id));
    }
}
