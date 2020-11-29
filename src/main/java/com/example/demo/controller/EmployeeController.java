package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Employee employee = this.employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found for this id: " + id));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        Boolean valid = employee.validateSelf();
        if (valid) {
            this.employeeRepository.save(employee);
            return ResponseEntity.ok().body(HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable(value = "id") Long id,
                                            @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Boolean valid = employeeDetails.validateSelf();
        if (id != null && valid) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("user not found for this id: " + id));
            employee.setEmail(employeeDetails.getEmail());
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            this.employeeRepository.save(employee);
            return ResponseEntity.ok().body(HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) throws ResourceNotFoundException {
        if (id != null) {
            Employee employee = this.employeeRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Laptop not found for this id: " + id));
            this.employeeRepository.delete(employee);
            return ResponseEntity.ok().body(HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
}
