package com.rc.mentorship.workplace_reservation.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.UserNotFoundException;
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
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Setter
    private static boolean disabled = false;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            ErrorSerializationUtil.setResponseBody(response,
                    "No JWT token in Authorization header!", HttpStatus.UNAUTHORIZED);
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
            ErrorSerializationUtil.setResponseBody(response,
                    "Invalid JWT token!", HttpStatus.UNAUTHORIZED);
            return;
        } catch (UserNotFoundException ex) {
            ErrorSerializationUtil.setResponseBody(response,
                    ex.getMessage(), HttpStatus.UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return disabled;
    }
}
