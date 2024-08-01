package com.rc.mentorship.workplace_reservation.exception;

public class BadCredentialsException extends RuntimeException {
    private static final String MESSAGE = "Wrong email or password!";

    public BadCredentialsException() {
        super(MESSAGE);
    }
}
