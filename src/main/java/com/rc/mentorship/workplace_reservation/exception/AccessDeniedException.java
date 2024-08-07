package com.rc.mentorship.workplace_reservation.exception;

public class AccessDeniedException extends RuntimeException {
    public static final String MESSAGE = "Access denied!";

    public AccessDeniedException() {
        super(MESSAGE);
    }
}
