package com.javatechie.keycloak;

import com.javatechie.keycloak.entity.Employee;
import com.javatechie.keycloak.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@SpringBootApplication
@RestController
public class SpringBootKeycloakExampleApplication {

    @Autowired
    private EmployeeService service;

    //this method can be accessed by user whose role is user
    @GetMapping("/{employeeId}")
    @RolesAllowed("user")
    public ResponseEntity<Employee> getEmployee(@PathVariable int employeeId) {
        return ResponseEntity.ok(service.getEmployee(employeeId));
    }

    //this method can be accessed by user whose role is admin
    @GetMapping("/list")
    @RolesAllowed("user") 
    public ResponseEntity<List<Employee>> findALlEmployees() {
        return ResponseEntity.ok(service.getAllEmployees());
    }
    
    @GetMapping(path = "/")
	public String index() {
	    return "This my public RSA key 987654321";
	}
	    
	@GetMapping(path = "/secret")
	@RolesAllowed("testing")
	public String customers() {
	    return "This my private RSA key 789456231";
	}

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKeycloakExampleApplication.class, args);
    }

}
