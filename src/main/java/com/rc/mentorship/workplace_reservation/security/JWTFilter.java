package com.rc.mentorship.workplace_reservation.security;

import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import com.rc.mentorship.workplace_reservation.util.ErrorSerializationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {
//    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer")) {
            String jwt = authHeader.substring(6);

            if (jwt.isBlank()) {
                setResponseBody(response, "Blank jwt!");
                return;
            } else {
//                String username = jwtUtil.retrieveClaim(jwt);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setResponseBody(HttpServletResponse response, String message) throws IOException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorDetails errorDetails =
                new ErrorDetails(status.value(), status.name(), message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(ErrorSerializationUtil.convertToJson(errorDetails));
    }
}
