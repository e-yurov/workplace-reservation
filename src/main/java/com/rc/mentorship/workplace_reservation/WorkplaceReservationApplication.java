package com.rc.mentorship.workplace_reservation;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
		name = "Keycloak",
		openIdConnectUrl = "http://localhost:8180/realms/workplace_reservation/.well-known/openid-configuration",
		scheme = "bearer",
		type = SecuritySchemeType.OPENIDCONNECT,
		in = SecuritySchemeIn.HEADER
//		, flows = @OAuthFlows(password = @OAuthFlow(
//				authorizationUrl = "http://localhost:8180/realms/workplace_reservation/auth",
//				scopes = {
//						@OAuthScope(name = "read_access", description = "read data"),
//						@OAuthScope(name = "write_access", description = "modify data")
//				}
//		))
)
public class WorkplaceReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkplaceReservationApplication.class, args);
	}

}
