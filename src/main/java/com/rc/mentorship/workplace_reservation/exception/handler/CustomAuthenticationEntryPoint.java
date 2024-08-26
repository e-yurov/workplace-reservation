package com.rc.mentorship.workplace_reservation.exception.handler;

import com.rc.mentorship.workplace_reservation.util.ErrorSerializationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        ErrorSerializationUtil.setResponseBody(response, exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
