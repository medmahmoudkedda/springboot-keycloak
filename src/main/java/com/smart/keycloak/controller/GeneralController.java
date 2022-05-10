package com.smart.keycloak.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smart.keycloak.entity.Employee;
import com.smart.keycloak.entity.EntrepriseDTO;
import com.smart.keycloak.service.EmployeeService;
import com.smart.keycloak.service.KeycloakAdminServices;

@RestController
public class GeneralController {

	@Autowired
	private EmployeeService service;
	
	@Autowired
	private KeycloakAdminServices keycloakAdminServices;


	// this method can be accessed by user whose role is user
	@GetMapping("/{employeeId}")
	// @RolesAllowed("user")
	@PreAuthorize("hasAuthority('ROLE_user')")
	public ResponseEntity<Employee> getEmployee(@PathVariable int employeeId) {
		return ResponseEntity.ok(service.getEmployee(employeeId));
	}

	// this method can be accessed by user whose role is admin
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ROLE_admin')")
	public ResponseEntity<List<Employee>> findALlEmployees() {
		return ResponseEntity.ok(service.getAllEmployees());
	}

	@GetMapping(path = "/")
	public String index() {
		return "This my public RSA key 987654321";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout();
		return "U R OUT";
	}

	@GetMapping(path = "/secret")
	@PreAuthorize("hasRole('user') and @securityService.hasPermission()")
	public String customers() {
		return "This my private RSA key 789456231";
	}

	@GetMapping(path = "/resource")
	@PreAuthorize("hasRole('bank_teller')")
	public String testresource() {
		return "This is resource test";
	}

	@GetMapping(path = "/compte")
	@PreAuthorize("@securityService.hasPermission()")
	public String account() {
		return "This is resource account protected";
	}

	@GetMapping(path = "/timedservice")
	@PreAuthorize("hasRole('bank_teller')")
	public String timedservice() {
		return "This resource is timed in";
	}
	
	@PostMapping("/addentreprise")
    public String addUser(@RequestBody EntrepriseDTO userDTO){
		System.out.println("test");
		keycloakAdminServices.addEntreprise(userDTO);
        return "User Added Successfully.";
    }
	
	@GetMapping(path = "/getroles")
	public List<String> getroles() {
		return keycloakAdminServices.getAllRoles();
	}
	
	@GetMapping(path = "/addrole")
	public String addRole(@Param("rolename") String rolename) {
		return keycloakAdminServices.addRealmRole(rolename);
	}
	

	@Component("securityService")
	public class SecurityService {

		public boolean hasPermission() {

			// Collection<? extends GrantedAuthority> authorities =
			// SecurityContextHolder.getContext().getAuthentication().getAuthorities();

			KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
			AccessToken accessToken = session.getToken();
			String username = accessToken.getPreferredUsername();
			String emailID = accessToken.getEmail();
			String lastname = accessToken.getFamilyName();
			String firstname = accessToken.getGivenName();
			String realmName = accessToken.getIssuer();
			Access realmAccess = accessToken.getRealmAccess();
			Set<String> roles = realmAccess.getRoles();
			
			
			System.out.println(username + " " + emailID + " " + lastname + " " + firstname + " " + realmName + " "
					+ realmAccess + " " + roles);
			
			/////
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			
			System.out.println("Request  = " + request);

			KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) request
					.getAttribute(KeycloakSecurityContext.class.getName());
			System.out.println("ksc = " + keycloakSecurityContext);
			System.out.println(keycloakSecurityContext.getAuthorizationContext());
			AuthorizationContext authzContext = keycloakSecurityContext.getAuthorizationContext();

			// AuthorizationContext authzContext = session.getAuthorizationContext();
//			  if (authzContext.hasScopePermission("brands:write")) {
//			 
//			    																
//			  }

			if (authzContext.hasResourcePermission("View Account Resource Permission")) {
				System.out.println("Your are allowed");
				return true;

			}

			////

			System.out.println(username + " " + emailID + " " + lastname + " " + firstname + " " + realmName + " "
					+ realmAccess + " " + roles);

			Map<String, Object> customClaims = accessToken.getOtherClaims();

			System.out.println(
					"Line = " + principal.getKeycloakSecurityContext().getToken().getOtherClaims().get("project"));

			if (customClaims.containsKey("project")) {
				String pro = String.valueOf(customClaims.get("project"));
				System.out.println(pro);
				return true;
			}

			System.out.println(
					"Line = " + principal.getKeycloakSecurityContext().getToken().getOtherClaims().get("project"));
//	        for (PermissionEnum permission : permissions) {
//	            if (authorities.contains(new SimpleGrantedAuthority(permission.toString))) {
//	                return true;
//	            }
//	        }
			// authorities.iterator().forEachRemaining(t ->
			// System.out.println(t.toString()));
			return false;
		}
	}

}
