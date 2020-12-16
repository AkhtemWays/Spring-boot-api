package com.example.demo.DAO;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Laptop;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Repository
public interface LaptopsDAO {

    List<Laptop> getAllLaptops();

    public void createLaptop(Long parentId, Laptop laptop) throws EntityNotFoundException;

    public Laptop getLaptopById(Long id);

    public String updateLaptopById(Laptop laptopToUpdate) throws ResourceNotFoundException;

    public String deleteLaptopById(Long id) throws ResourceNotFoundException;

    Session getCurrentSession();

    Session getOpenedSession();
}
