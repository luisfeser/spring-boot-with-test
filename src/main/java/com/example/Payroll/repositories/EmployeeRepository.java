/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Payroll.repositories;

import com.example.Payroll.entities.Employee;

import io.micrometer.common.lang.NonNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lfse01
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // todo corregir que realmente no se pueda llamar a este método y de un error controlado 
    @Override
        @RestResource(exported = false)
        default void deleteById(@NonNull Long id) {
            throw new UnsupportedOperationException("Eliminar productos no está permitido.");
        }
}
