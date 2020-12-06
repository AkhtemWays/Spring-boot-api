package com.example.demo.shared;

import com.example.demo.model.Department;

import java.util.List;

public class DepartmentControllerPostRequestBodyType {
    private Department department;
    private List<Long> employeeIds;

    public DepartmentControllerPostRequestBodyType() {
    }

    public DepartmentControllerPostRequestBodyType(Department department, List<Long> employeeIds) {
        this.department = department;
        this.employeeIds = employeeIds;
    }

    @Override
    public String toString() {
        return "DepartmentControllerPostRequestBodyType{" +
                "department=" + department +
                ", employeeIds=" + employeeIds +
                '}';
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
