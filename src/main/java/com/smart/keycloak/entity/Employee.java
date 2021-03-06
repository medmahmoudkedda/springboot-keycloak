package com.smart.keycloak.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private double salary;
    
    public Employee(){}
    
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
}
