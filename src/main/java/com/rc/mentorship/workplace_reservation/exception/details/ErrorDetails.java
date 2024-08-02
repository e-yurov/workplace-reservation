package com.rc.mentorship.workplace_reservation.exception.details;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorDetails {
    private final int code;
    private final String status;
    private final String message;
    private final LocalDateTime dateTime;

    public ErrorDetails(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
