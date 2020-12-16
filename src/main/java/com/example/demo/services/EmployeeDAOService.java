package com.example.demo.services;

import com.example.demo.DAO.EmployeeDAO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class EmployeeDAOService implements EmployeeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void createEmployee(Employee employee) throws NullPointerException {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.save(employee);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
                e.printStackTrace();
            } else {
                throw new NullPointerException();
            }
        }
    }

    public List<Employee> getAllEmployees() {
        Session session = null;
        try {
            session = this.getOpenedSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
            criteria.from(Employee.class);
            return session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Employee getEmployeeById(Long id) {
        try {
            Session session = this.getOpenedSession();
            return session.get(Employee.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateEmployee(Employee updatedEmployee, Long id) throws ResourceNotFoundException {
        try {
            Session session = this.getOpenedSession();
            Transaction tx = session.beginTransaction();
            Employee employee = session.load(Employee.class, id);
            employee.setFirstName(updatedEmployee.getFirstName());
            employee.setLastName(updatedEmployee.getLastName());
            employee.setEmail(updatedEmployee.getEmail());
            session.update(employee);
            tx.commit();
            session.close();
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("not found employee with id: " + id);
        }
    }

    public String deleteEmployee(Long id) throws ResourceNotFoundException {
        Session session = this.getOpenedSession();
        Transaction transaction = session.beginTransaction();
        Query q = session.createQuery("delete Employee where id = " + id);
        int result = q.executeUpdate();
        if (result == 0) {
            transaction.commit();
            session.close();
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        } else {
            transaction.commit();
            session.close();
            return "ok";
        }
    }

    public Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public Session getOpenedSession() {
        return this.sessionFactory.openSession();
    }
}
