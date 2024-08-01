package com.rc.mentorship.workplace_reservation.config;

import com.rc.mentorship.workplace_reservation.security.JWTFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterRegistration(JWTFilter jwtFilter) {
        FilterRegistrationBean<JWTFilter> registrationBean =
                new FilterRegistrationBean<>();

        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
