package com.rc.mentorship.workplace_reservation.exception;

public class FiltrationParamsFormatException extends RuntimeException {
    private static final String MESSAGE = "Wrong request filtration parameters format for parameter '%s'!";

    public FiltrationParamsFormatException(String paramName) {
        super(String.format(MESSAGE, paramName));
    }
}
