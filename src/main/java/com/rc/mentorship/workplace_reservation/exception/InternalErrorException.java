package com.rc.mentorship.workplace_reservation.exception;

public class InternalErrorException extends RuntimeException {
    private static final String MESSAGE = "Internal server error";

    public InternalErrorException() {
        super(MESSAGE);
    }

    public InternalErrorException(String message) {
        super(message);
    }
}
