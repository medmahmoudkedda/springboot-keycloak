package com.smart.keycloak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smart.keycloak.entity.Employee;
import com.smart.keycloak.service.EmployeeService;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

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
	
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "U R OUT";
    }
    
   
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public String exceptionHandler() {
//        return "SORRY U R NOT ALLOWED";
//    }
    
	@GetMapping(path = "/secret")
	@RolesAllowed("admin")
	public String customers() {
	    return "This my private RSA key 789456231";
	}

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKeycloakExampleApplication.class, args);
    }

}
