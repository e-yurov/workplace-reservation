package com.rc.mentorship.workplace_reservation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@UtilityClass
public class ErrorSerializationUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String convertToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }

        return mapper.writeValueAsString(object);
    }

    public void setUnauthorizedResponseBody(HttpServletResponse response, String message) throws IOException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorDetails errorDetails =
                new ErrorDetails(status.value(), status.name(), message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(ErrorSerializationUtil.convertToJson(errorDetails));
    }
}
