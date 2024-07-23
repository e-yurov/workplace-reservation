package com.rc.mentorship.workplace_reservation.exception.handler;

import com.rc.mentorship.workplace_reservation.exception.*;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }

    @ExceptionHandler(BadReservationRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadReservationRequestException(
            BadReservationRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }

    @ExceptionHandler(BadReservationTimeException.class)
    public ResponseEntity<ErrorDetails> handleBadReservationTimeException(
            BadReservationTimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }

    @ExceptionHandler(WorkplaceNotAvailableException.class)
    public ResponseEntity<ErrorDetails> handleWorkplaceNotAvailableException(
            WorkplaceNotAvailableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }

    @ExceptionHandler(FiltrationParamsFormatException.class)
    public ResponseEntity<ErrorDetails> handeFiltrationParamsFormatException(
            FiltrationParamsFormatException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }
}
