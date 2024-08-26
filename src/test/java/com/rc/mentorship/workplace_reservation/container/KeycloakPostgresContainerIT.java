package com.rc.mentorship.workplace_reservation.container;

import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public abstract class KeycloakPostgresContainerIT extends BasePostgresContainerIT {
    public KeycloakPostgresContainerIT(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    private static final KeycloakContainer keycloak =
            new KeycloakContainer("quay.io/keycloak/keycloak:25.0.2")
                    .withRealmImportFile("keycloak/realm-export.json");
//                    .withReuse(true);
//    @BeforeEach
//    void beforeEach() {
//        System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri",
//                keycloak.getAuthServerUrl() + "/realms/workplace_reservation");
//        System.setProperty("spring.security.oauth2.client.provider.keycloak.issuer-uri",
//                keycloak.getAuthServerUrl() + "/realms/workplace_reservation");
//        System.setProperty("keycloak.server-url", keycloak.getAuthServerUrl());
//    }

    static {keycloak.start();}


    @DynamicPropertySource
    static void registerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/workplace_reservation");
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/workplace_reservation");
        registry.add("keycloak.server-url", keycloak::getAuthServerUrl);
    }

    protected String getAdminToken() {
        String uri = keycloak.getAuthServerUrl() + "/realms/workplace_reservation/protocol/openid-connect/token";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", "admin");
        body.add("password", "admin");
        body.add("grant_type", "password");
        body.add("client_id", "reservation-app");
        body.add("client_secret", "VRjLSsAD4t9r6Dw8DeBK4oVE2z4gZyFo");

        WebClient client = WebClient.builder().build();
        Map<String, String> result = client.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>(){})
                .block();

        return result.get("access_token");
    }
}
