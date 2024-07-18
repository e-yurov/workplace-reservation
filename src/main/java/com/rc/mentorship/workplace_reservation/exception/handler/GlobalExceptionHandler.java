package com.rc.mentorship.workplace_reservation.exception.handler;

import com.rc.mentorship.workplace_reservation.exception.ResourceNotFoundException;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> handleParameterConvertingException(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), ex.getMessage());
        return ResponseEntity.status(status).body(details);
    }
}
