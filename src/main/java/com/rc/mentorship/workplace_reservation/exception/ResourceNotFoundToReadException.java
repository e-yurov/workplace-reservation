package com.rc.mentorship.workplace_reservation.exception;

public class ResourceNotFoundToReadException extends RuntimeException {
    private static final String MESSAGE = "No %s with such ID to read!";

    public ResourceNotFoundToReadException(String type) {
        super(String.format(MESSAGE, type));
    }
}
