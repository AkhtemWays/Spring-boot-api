package com.example.demo.services;

import com.example.demo.DAO.DepartmentsDAO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.types.DepartmentControllerPostRequestBodyType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentsDAOService implements DepartmentsDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Department> getAllDepartments() {
        Session session = null;
        try {
            session = this.getOpenedSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Department> criteria = builder.createQuery(Department.class);
            criteria.from(Department.class);
            return session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Department getDepartmentById(Long id) throws ResourceNotFoundException {
        try {
            Session session = this.getOpenedSession();
            return session.get(Department.class, id);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("not found for this id: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createDepartment(DepartmentControllerPostRequestBodyType body) throws ResourceNotFoundException {
        Session session = null;
        try {
            session = this.getOpenedSession();
            session.beginTransaction();
            List<Employee> employees = new ArrayList<>();
            Session finalSession = session;
            body.getEmployeeIds()
                    .forEach((Long employeeId) -> employees.add(finalSession.load(Employee.class, employeeId)));
            Department department = new Department(
                    body.getDepartment().getDivision(), employees
            );
            session.saveOrUpdate(department);
            employees
                    .forEach((Employee employee) -> employee.getDepartments().add(department));
            session.getTransaction().commit();
            session.close();
            return "OK";
        } catch (EntityNotFoundException e) {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
            throw new ResourceNotFoundException("not found for one of those ids: " + body.getEmployeeIds());
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
            e.printStackTrace();
            return null;
        }
    }

    public String deleteById(Long id) throws ResourceNotFoundException {
        Session session = this.getOpenedSession();
        Transaction transaction = session.beginTransaction();
        Query q = session.createQuery("delete Department where id = " + id);
        int result = q.executeUpdate();
        if (result == 0) {
            transaction.commit();
            session.close();
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        } else {
            transaction.commit();
            session.close();
            return "OK";
        }
    }

    public Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public Session getOpenedSession() {
        return this.sessionFactory.openSession();
    }
}
