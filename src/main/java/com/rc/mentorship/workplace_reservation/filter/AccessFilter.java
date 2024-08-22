package com.rc.mentorship.workplace_reservation.filter;

import com.rc.mentorship.workplace_reservation.security.config.util.RequestMatcher;
import com.rc.mentorship.workplace_reservation.util.ErrorSerializationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
@RequiredArgsConstructor
public class AccessFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        boolean match = requestMatcher.match(
                HttpMethod.valueOf(request.getMethod()),
                request.getRequestURI());
        if (!match) {
            ErrorSerializationUtil.setResponseBody(response,
                    "No access!", HttpStatus.UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
