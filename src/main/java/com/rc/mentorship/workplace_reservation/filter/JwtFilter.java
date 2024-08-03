package com.rc.mentorship.workplace_reservation.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.UserNotFoundException;
import com.rc.mentorship.workplace_reservation.exception.details.ErrorDetails;
import com.rc.mentorship.workplace_reservation.security.auth.UserAuthentication;
import com.rc.mentorship.workplace_reservation.security.auth.UserDetailsService;
import com.rc.mentorship.workplace_reservation.security.context.SecurityContextHolder;
import com.rc.mentorship.workplace_reservation.service.JwtService;
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

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            setResponseBody(response, "No JWT token in Authorization header!");
            return;
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtService.verifyAndRetrieveEmail(token);
            User user = userDetailsService.loadUserByEmail(email);
            UserAuthentication authentication =
                    new UserAuthentication(user.getEmail(), user.getPassword(), user);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JWTVerificationException ex) {
            setResponseBody(response, "Invalid JWT token!");
            return;
        } catch (UserNotFoundException ex) {
            setResponseBody(response, ex.getMessage());
            return;
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/v1/auth") || uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs");
    }
}
