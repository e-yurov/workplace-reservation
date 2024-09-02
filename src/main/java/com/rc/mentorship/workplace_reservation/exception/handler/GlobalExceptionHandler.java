package com.rc.mentorship.workplace_reservation.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.mentorship.workplace_reservation.exception.*;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundException(NotFoundException ex) {
        return buildErrorDetails(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
            BadReservationRequestException.class,
            BadReservationTimeException.class,
            WorkplaceNotAvailableException.class,
            FiltrationParamsFormatException.class,
    })
    public ResponseEntity<ErrorDetails> handeBadRequest(
            RuntimeException ex) {
        return buildErrorDetails(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignException(FeignException ex) {
        if (ex.status() == -1) {
            return handleInternalError(new InternalErrorException("User service is not responding!"));
        }

        ErrorDetails errorDetails;
        try {
            errorDetails = objectMapper.readValue(ex.contentUTF8(), ErrorDetails.class);
        } catch (JsonProcessingException e) {
            return handleInternalError(new InternalErrorException());
        }
        return ResponseEntity.status(errorDetails.getCode()).body(errorDetails);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ErrorDetails> handleInternalError(InternalErrorException ex) {
        return buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorDetails> buildErrorDetails(HttpStatus status, String message) {
        ErrorDetails details = new ErrorDetails(status.value(), status.name(), message);
        return ResponseEntity.status(status).body(details);
    }
}
