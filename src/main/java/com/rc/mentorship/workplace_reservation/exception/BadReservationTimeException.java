package com.rc.mentorship.workplace_reservation.exception;

public class BadReservationTimeException extends RuntimeException {
    private static final String MESSAGE = "Wrong reservation time! Time of start must be before time of end!";

    public BadReservationTimeException() {
        super(MESSAGE);
    }
}
