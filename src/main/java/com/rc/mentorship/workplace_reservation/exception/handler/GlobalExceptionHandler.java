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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }

    @ExceptionHandler({
            BadReservationRequestException.class,
            BadReservationTimeException.class,
            WorkplaceNotAvailableException.class,
            FiltrationParamsFormatException.class,
            UserAlreadyExistsException.class
    })
    public ResponseEntity<ErrorDetails> handeBadRequest(
            RuntimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }
}
