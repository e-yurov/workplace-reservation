package com.rc.mentorship.workplace_reservation.config;

import com.rc.mentorship.workplace_reservation.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registrationBean =
                new FilterRegistrationBean<>();

        registrationBean.setFilter(jwtFilter);

        return registrationBean;
    }
}
