package com.smart.keycloak.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smart.keycloak.entity.EntrepriseDTO;

@Service
public class KeycloakAdminServices {
	static Keycloak keycloak = null;
	
	@Value("${keycloak.auth-server-url}")
	private String serverUrl;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.resource}")
	private String clientId;
	@Value("${keycloak.credentials.secret}")
	private String clientSecret;
	@Value("${admin.username}")
	private String userName;
	@Value("${admin.password}")
	private String password;

	public KeycloakAdminServices() {
		if (keycloak == null) {

			keycloak = KeycloakBuilder.builder().serverUrl(serverUrl).realm(realm).grantType(OAuth2Constants.PASSWORD)
					.username(userName).password(password).clientId(clientId).clientSecret(clientSecret)
					.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
		}
	}

	public List<String> getAllRoles() {
		ClientRepresentation clientRep = keycloak.realm(realm).clients().findByClientId(clientId).get(0);
		List<String> availableRoles = keycloak.realm(realm).clients().get(clientRep.getId()).roles().list().stream()
				.map(role -> role.getName()).collect(Collectors.toList());
		return availableRoles;
	}

	public String addRealmRole(String new_role_name) {

		if (!getAllRoles().contains(new_role_name)) {
			RoleRepresentation roleRep = new RoleRepresentation();
			roleRep.setName(new_role_name);
			roleRep.setDescription("role_" + new_role_name);
			ClientRepresentation clientRep = keycloak.realm(realm).clients().findByClientId(clientId).get(0);
			keycloak.realm(realm).clients().get(clientRep.getId()).roles().create(roleRep);

			return "ROLE_ADDED";
		} else {
			return "ROLE_EXIST";
		}
	}

	public void makeComposite(String role_name) {
		ClientRepresentation clientRep = keycloak.realm(realm).clients().findByClientId(clientId).get(0);
		RoleRepresentation role = keycloak.realm(realm).clients().get(clientRep.getId()).roles().get(role_name)
				.toRepresentation();
		List<RoleRepresentation> composites = new LinkedList<RoleRepresentation>();
		composites.add(keycloak.realm(realm).roles().get("offline_access").toRepresentation());
		// Error here keycloak.realm(realm).rolesById().addComposites(composites);
	}

	public void addEntreprise(EntrepriseDTO entrepriseDTO) {
		CredentialRepresentation credential = Credentials.createPasswordCredentials(entrepriseDTO.getPassword());
		UserRepresentation entreprise = new UserRepresentation();
		entreprise.setUsername(entrepriseDTO.getUsername());
		// entreprise.setFirstName(entrepriseDTO.getFirstname());
		// entreprise.setLastName(entrepriseDTO.getLastName());
		entreprise.setEmail(entrepriseDTO.getEmail());
		entreprise.setCredentials(Collections.singletonList(credential));
		entreprise.setEnabled(true);

		UsersResource instance = (UsersResource) keycloak;
		instance.create(entreprise);
	}

}
