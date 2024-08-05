package com.rc.mentorship.workplace_reservation.security.config;

import com.rc.mentorship.workplace_reservation.security.config.builders.HttpSecurity;
import com.rc.mentorship.workplace_reservation.security.config.util.RequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Bean
    public RequestMatcher securityFilterConfig(HttpSecurity http) {
        http.authorizeRequests()
                .requestMatchers("/swagger-ui", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
