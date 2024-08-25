package com.rc.mentorship.workplace_reservation.container;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@Testcontainers
//@RequiredArgsConstructor
public class KeycloakTestContainer extends PostgresTestContainer {
//    private final TestRestTemplate restTemplate;
//    private final Keycloak k;
//
//    @Autowired
//    public KeycloakTestContainer(TestRestTemplate restTemplate,
//                                 Keycloak k) {
//        this.restTemplate = restTemplate;
//        this.k = k;
//    }

    @LocalServerPort
    static int port;

    @Container
    private static final KeycloakContainer keycloak;

    static {
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:25.0.2")
            .withRealmImportFile("keycloak/realm-export.json");
        keycloak.start();
    }

    @DynamicPropertySource
    static void registerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/workplace_reservation");
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/workplace_reservation");
        registry.add("keycloak.server-url", keycloak::getAuthServerUrl);
    }

//    @Test
//    void testKc() {
//        assertTrue(keycloak.isRunning());
//
//        String authServerUrl = keycloak.getAuthServerUrl();
//        System.out.println(authServerUrl);
//
//        String uri = keycloak.getAuthServerUrl() + "/realms/workplace_reservation/protocol/openid-connect/token";
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("username", "admin");
//        body.add("password", "admin");
//        body.add("grant_type", "password");
//        body.add("client_id", "reservation-app");
//        body.add("client_secret", "VRjLSsAD4t9r6Dw8DeBK4oVE2z4gZyFo");
//
////        body.put("username", Collections.singletonList("admin"));
////        body.put("password", Collections.singletonList("admin"));
////        body.put("grant_type", Collections.singletonList("password"));
////        body.put("client_id", Collections.singletonList("reservation-app"));
//
//        WebClient client = WebClient.builder().build();
//        Map<String, String> result = client.post()
//                .uri(uri)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters.fromFormData(body))
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>(){})
//                .block();
//
//        System.out.println(result);
//
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
////
//
////
////        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, );
////
////        restTemplate.exchange(request, )
//    }

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
