package com.rc.mentorship.workplace_reservation.exception;

public class ResourceNotFoundToUpdateException extends RuntimeException {
    private static final String MESSAGE = "No %s with such ID to update!";

    public ResourceNotFoundToUpdateException(String type) {
        super(String.format(MESSAGE, type));
    }
}
