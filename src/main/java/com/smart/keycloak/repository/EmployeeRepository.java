package com.smart.keycloak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.keycloak.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
	
}
