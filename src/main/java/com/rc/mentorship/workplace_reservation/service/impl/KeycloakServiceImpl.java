package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.request.RegisterRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    @Value("${keycloak.realm}")
    private String realm;
    private final Keycloak keycloak;

    private UsersResource usersResource;

    public UserResponse fillUserResponse(UserResponse response) {
        List<String> roles = getUsersResource().get(getKeycloakIdByEmail(response.getEmail()))
                .roles().realmLevel().listAll()
                .stream()
                .map(RoleRepresentation::getName).toList();
        response.setRoles(roles);
        return response;
    }

    public void addUser(RegisterRequest request) {
        CredentialRepresentation credential = createPasswordCredentials(request.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        UsersResource usersResource = getUsersResource();
        usersResource.create(user);
        addRealmRoleToUser(request.getEmail(), "USER");
    }

    public void updateUser(User user) {
        String id = user.getKeycloakId();
        UserRepresentation representation = getUsersResource().get(id).toRepresentation();
        representation.setEmail(user.getEmail());
        representation.setUsername(user.getName());
        getUsersResource().get(id).update(representation);
    }

    public void deleteUserById(String id) {
        getUsersResource().delete(id);
    }

    @Override
    public void deleteUserByEmail(String email) {
        List<UserRepresentation> users = getUsersResource().searchByEmail(email, true);
        if (!users.isEmpty()) {
            String id = users.get(0).getId();
            getUsersResource().delete(id);
        }
    }

    public String getKeycloakIdByEmail(String email) {
        return getUsersResource().searchByEmail(email, true).get(0).getId();
    }

    @Override
    public UserRepresentation getKeycloakUserByEmail(String email) {
        return getUsersResource().searchByEmail(email, true).get(0);
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredential = new CredentialRepresentation();
        passwordCredential.setTemporary(false);
        passwordCredential.setType(CredentialRepresentation.PASSWORD);
        passwordCredential.setValue(password);
        return passwordCredential;
    }

    private void addRealmRoleToUser(String email, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        List<UserRepresentation> users = getUsersResource().searchByEmail(email, true);
        UserResource userResource = getUsersResource().get(users.get(0).getId());
        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
        RoleMappingResource roleMappingResource = userResource.roles();
        roleMappingResource.realmLevel().add(Collections.singletonList(role));
    }

    private UsersResource getUsersResource() {
        if (usersResource == null) {
            usersResource = keycloak.realm(realm).users();
        }
        return usersResource;
    }
}
