package com.rc.mentorship.workplace_reservation.config;

import com.rc.mentorship.workplace_reservation.filter.AccessFilter;
import com.rc.mentorship.workplace_reservation.filter.AuthEnablingFilter;
import com.rc.mentorship.workplace_reservation.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
//    @Bean
    public FilterRegistrationBean<AuthEnablingFilter> authEnablingFilterRegistration(
            AuthEnablingFilter authEnablingFilter) {
        FilterRegistrationBean<AuthEnablingFilter> registrationBean =
                new FilterRegistrationBean<>();
        registrationBean.setFilter(authEnablingFilter);
        registrationBean.setOrder(Integer.MIN_VALUE);

        return registrationBean;
    }

//    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registrationBean =
                new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.setOrder(Integer.MIN_VALUE + 1);

        return registrationBean;
    }

//    @Bean
    public FilterRegistrationBean<AccessFilter> accessFilterRegistration(AccessFilter accessFilter) {
        FilterRegistrationBean<AccessFilter> registrationBean =
                new FilterRegistrationBean<>();
        registrationBean.setFilter(accessFilter);
        registrationBean.setOrder(Integer.MIN_VALUE + 2);

        return registrationBean;
    }
}
