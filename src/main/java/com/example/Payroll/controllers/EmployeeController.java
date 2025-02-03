/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Payroll.controllers;

import com.example.Payroll.entities.Employee;
import com.example.Payroll.repositories.EmployeeRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author lfse01
 * TODO: hacerlo con responseEntity
 */
@RestController
public class EmployeeController {
    
    private final EmployeeRepository employeeRepository;
    
    EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @GetMapping("/employees")
    ResponseEntity<List<Employee>> all() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }
    
    @PostMapping("/employees")
    ResponseEntity<Employee> newEmployee(@RequestBody Employee newEmployee) {
        Employee savedEmployee = employeeRepository.save(newEmployee);
        
        // genero la ubicación de la entidad creada
        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmployee.getId())
                .toUri();
        
        // Retornar respuesta con código 201 Created, cabecera Location y cuerpo del recurso
        return ResponseEntity.created(ubicacion).body(savedEmployee);
    }
    
    @GetMapping("/employees/{id}")
    ResponseEntity<Employee> one(@PathVariable Long id) {
        
        Optional<Employee> Employee = employeeRepository.findById(id);
        
        if (Employee == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.of(Employee);
    }
    
    @PutMapping("/employees/{id}")
    ResponseEntity<Employee> replaceEmployee(@RequestBody Employee modifiedEmployee, 
        @PathVariable Long id) {
        
        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        
        return employeeRepository.findById(id)
            .map(employee -> {
                employee.setName(modifiedEmployee.getName());
                employee.setRole(modifiedEmployee.getRole());
                Employee updatedEmployee = employeeRepository.save(employee);
                // no puedo devolver un "no content" porque entonces en el else no llegaría bien el employee
                return ResponseEntity.ok().location(ubicacion).body(updatedEmployee); 
            })
            .orElse(ResponseEntity.created(ubicacion).body(employeeRepository.save(modifiedEmployee))); 
     }
    
    
    @DeleteMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    void deleteEmployee(@PathVariable Long id) {
    }
    
}
