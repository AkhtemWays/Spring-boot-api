package com.example.demo.controller;

import com.example.demo.services.DepartmentsDAOService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.types.DepartmentControllerPostRequestBodyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    @Autowired
    private DepartmentsDAOService departmentsDAOService;

    @GetMapping
    public ResponseEntity<?> getAllDepartments() {
        List<Department> departments = this.departmentsDAOService.getAllDepartments();
        return departments == null
                ? ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR)
                : ResponseEntity.ok().body(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        if (id != null) {
            Department department = this.departmentsDAOService.getDepartmentById(id);
            return department == null
                    ? ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR)
                    : ResponseEntity.ok().body(department);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentControllerPostRequestBodyType body) throws ResourceNotFoundException {
        if (body.getDepartment().getDivision().trim().equals("") || body.getEmployeeIds() == null) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        String response = this.departmentsDAOService.createDepartment(body);
        if (response.equals("OK")) {
            return ResponseEntity.ok().body(HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartmentById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        if (id != null) {
            String response = this.departmentsDAOService.deleteById(id);
            if (response.equals("OK")) {
                return ResponseEntity.ok().body(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
}
