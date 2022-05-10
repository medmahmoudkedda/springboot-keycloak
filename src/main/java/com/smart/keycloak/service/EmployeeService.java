package com.smart.keycloak.service;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.keycloak.entity.Employee;
import com.smart.keycloak.repository.EmployeeRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeService {
	static Keycloak keycloak = null;
	final static String serverUrl = "http://localhost:8080/auth";
	final static String realm = "admin-spring-boot";
	final static String clientId = "bank-api";
	final static String clientSecret = "RecgUJIemYjK9ybtH1Sp33hOroH3AcEj";
	final static String userName = "keyadmin";
	final static String password = "keypassword";

	@Autowired
	private EmployeeRepository employeeRepository;

	@PostConstruct
	public void initializeEmployeeTable() {
		employeeRepository.saveAll(
				Stream.of(new Employee("john", 20000), new Employee("mak", 55000), new Employee("peter", 120000))
						.collect(Collectors.toList()));
	}

	public Employee getEmployee(int employeeId) {
		return employeeRepository.findById(employeeId).orElse(null);
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	
}
