package com.rc.mentorship.workplace_reservation.exception.handler;

import com.rc.mentorship.workplace_reservation.util.ErrorSerializationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        ErrorSerializationUtil.setResponseBody(response, exception.getMessage(), HttpStatus.FORBIDDEN);
    }
}
