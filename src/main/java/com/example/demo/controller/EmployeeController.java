package com.example.demo.controller;

import com.example.demo.DAO.EmployeeDAO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @GetMapping
    public List<Employee> getAllEmployees() {
        return this.employeeDAO.getAllEmployees();
    }

    @Autowired
    private EmployeeDAO employeeDAO;

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Employee employee = this.employeeDAO.getEmployeeById(id);
        if (employee == null) {
            throw new ResourceNotFoundException("user not found for this id: " + id);
        }
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        Boolean valid = employee.validateSelf();
        if (valid) {
            this.employeeDAO.createEmployee(employee);
            return ResponseEntity.ok().body(HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable(value = "id") Long id,
                                            @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Boolean valid = employeeDetails.validateSelf();
        if (id != null && valid) {
            this.employeeDAO.updateEmployee(employeeDetails, id);
            return ResponseEntity.ok().body(HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) throws Exception {
        if (id != null) {
            try {
                String response = this.employeeDAO.deleteEmployee(id);
                return response.equals("ok")
                        ? ResponseEntity.ok().body(HttpStatus.OK)
                        : ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
}
