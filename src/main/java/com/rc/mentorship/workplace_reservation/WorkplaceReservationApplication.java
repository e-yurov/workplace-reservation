package com.rc.mentorship.workplace_reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class WorkplaceReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkplaceReservationApplication.class, args);
	}

}
