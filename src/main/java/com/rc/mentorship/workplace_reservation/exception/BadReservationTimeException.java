package com.rc.mentorship.workplace_reservation.exception;

public class BadReservationTimeException extends RuntimeException {
    private static final String MESSAGE = "Wrong reservation time! %s";

    public BadReservationTimeException(String messagePostfix) {
        super(String.format(MESSAGE, messagePostfix));
    }
}
