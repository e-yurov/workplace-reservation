package com.rc.mentorship.workplace_reservation.config;

import com.rc.mentorship.workplace_reservation.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoriesConfig {
    private final ApplicationContext appContext;
    private final String repositoryType;

    @Autowired
    public RepositoriesConfig(ApplicationContext appContext,
                              @Value("${repository_type}") String repositoryType) {
        this.appContext = appContext;
        this.repositoryType = repositoryType;
    }

    @Bean
    public LocationRepository locationRepository() {
        return appContext.getBean("location" + repositoryType, LocationRepository.class);
    }
}
