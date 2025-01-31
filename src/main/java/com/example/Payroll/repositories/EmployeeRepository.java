/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Payroll.repositories;

import com.example.Payroll.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author lfse01
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
}
