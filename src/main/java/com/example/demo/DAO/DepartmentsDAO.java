package com.example.demo.DAO;


import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.types.DepartmentControllerPostRequestBodyType;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentsDAO {
    List<Department> getAllDepartments();

    Department getDepartmentById(Long id) throws ResourceNotFoundException;

    String createDepartment(DepartmentControllerPostRequestBodyType body) throws ResourceNotFoundException;

    String deleteById(Long id) throws ResourceNotFoundException;

    Session getCurrentSession();

    Session getOpenedSession();
}
