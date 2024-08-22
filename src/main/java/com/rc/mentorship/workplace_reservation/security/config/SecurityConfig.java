package com.rc.mentorship.workplace_reservation.security.config;

import com.rc.mentorship.workplace_reservation.security.config.builders.HttpSecurity;
import com.rc.mentorship.workplace_reservation.security.config.util.RequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration("deprecatedSecurityConfig")
public class SecurityConfig {
    @Bean
    public RequestMatcher securityFilterConfig(HttpSecurity http) {
//        http.authorizeRequests()
//                .requestMatchers("/api/v1/auth/**").permitAll()
//                .requestMatchers("/swagger-ui/**").permitAll()
//                .requestMatchers("/v3/api-docs/**").permitAll()
//                .requestMatchers("/h2-console/**").permitAll()
//                .anyRequest().authenticated();
        http.authorizeRequests().anyRequest().permitAll();

        return http.build();
    }

    @Bean
    public MessageDigest messageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256");
    }
}
