package com.rc.mentorship.workplace_reservation.filter;

import com.rc.mentorship.workplace_reservation.security.config.configurers.AccessGranters;
import com.rc.mentorship.workplace_reservation.security.config.configurers.MatchingEntry;
import com.rc.mentorship.workplace_reservation.security.config.util.RequestMatcher;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthEnablingFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        MatchingEntry matchingForUri = requestMatcher.getMatchingForUri(
                HttpMethod.valueOf(request.getMethod()),
                request.getRequestURI());
        System.out.println(request.getRequestURI());
        JwtFilter.setDisabled(matchingForUri == null ||
                matchingForUri.getAccessGranter().equals(AccessGranters.AUTHENTICATED));
        filterChain.doFilter(request, response);
    }
}
