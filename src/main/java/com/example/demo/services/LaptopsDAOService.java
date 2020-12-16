package com.example.demo.services;

import com.example.demo.DAO.LaptopsDAO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.Laptop;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class LaptopsDAOService implements LaptopsDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public Session getOpenedSession() {
        return this.sessionFactory.openSession();
    }

    public List<Laptop> getAllLaptops() {
        Session session = this.getOpenedSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Laptop> criteria = builder.createQuery(Laptop.class);
            criteria.from(Laptop.class);
            return session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            session.getTransaction().commit();
            session.close();
            e.printStackTrace();
            return null;
        }
    }

    public void createLaptop(Long parentId, Laptop laptop) throws EntityNotFoundException {
        Session session = this.getOpenedSession();
        session.beginTransaction();
        Employee employee = session.load(Employee.class, parentId);
        Laptop newLaptop = new Laptop(
                laptop.getLaptopType(), laptop.getGraphicsCard(), laptop.getProcessor(), employee
        );
        session.save(newLaptop);
        employee.getLaptops().add(newLaptop);
        session.getTransaction().commit();
        session.close();
    }

    public Laptop getLaptopById(Long id) {
        Session session = null;
        try {
            session = this.getOpenedSession();
            session.beginTransaction();
            Laptop laptop = session.get(Laptop.class, id);
            session.getTransaction().commit();
            session.close();
            return laptop;
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().commit();
                session.close();
                return null;
            }
            return null;
        }
    }

    public String updateLaptopById(Laptop laptopToUpdate) throws ResourceNotFoundException {
        Session session = null;
        try {
            session = this.getOpenedSession();
            session.beginTransaction();
            Laptop currentLaptop = session.load(Laptop.class, laptopToUpdate.getId());
            currentLaptop.setLaptopType(laptopToUpdate.getLaptopType());
            currentLaptop.setGraphicsCard(laptopToUpdate.getGraphicsCard());
            currentLaptop.setProcessor(laptopToUpdate.getProcessor());
            session.update(currentLaptop);
            session.getTransaction().commit();
            session.close();
            return "OK";
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("not found with thid id: " + laptopToUpdate.getId());
        }
        catch (Exception e) {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
                e.printStackTrace();
                return null;
            }
            e.printStackTrace();
            return null;
        }
    }

    public String deleteLaptopById(Long id) throws ResourceNotFoundException {
        Session session = this.getOpenedSession();
        Transaction transaction = session.beginTransaction();
        TypedQuery<Laptop> q = session.createQuery("delete Laptop where id = " + id);
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
}
