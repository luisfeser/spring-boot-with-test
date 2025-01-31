/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Payroll.controllers;

import com.example.Payroll.entities.Employee;
import com.example.Payroll.repositories.EmployeeRepository;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lfse01
 */
@RestController
public class EmployeeController {
    
    private final EmployeeRepository employeeRepository;
    
    EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @GetMapping("/employees")
    List<Employee> all() {
        return employeeRepository.findAll();
    }
    
    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }
    
    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
    
    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee modifiedEmployee, 
        @PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(modifiedEmployee.getName());
                    employee.setRole(modifiedEmployee.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    return employeeRepository.save(modifiedEmployee);
                });
    }
    
    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
    
}
