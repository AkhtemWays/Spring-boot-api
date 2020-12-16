package com.example.demo.DAO;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDAO {

    void createEmployee(Employee employee) throws NullPointerException;

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    void updateEmployee(Employee updatedEmployee, Long id) throws ResourceNotFoundException;

    String deleteEmployee(Long id) throws ResourceNotFoundException;

    Session getCurrentSession();

    Session getOpenedSession();
}
