package com.example.demo.controller;

import com.example.demo.services.LaptopsDAOService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Laptop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/laptops")
public class LaptopController {

    @Autowired
    private LaptopsDAOService laptopsDAOService;

    @GetMapping
    public ResponseEntity<?> getAllLaptops() {
        List<Laptop> laptops = this.laptopsDAOService.getAllLaptops();
        if (laptops == null) {
            return ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().body(laptops);
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<?> addLaptop(@PathVariable("employeeId") Long employeeId,
                                       @RequestBody Laptop laptop) {
        Boolean valid = laptop.validateSelf();
        if (valid && employeeId != null) {
            try {
                this.laptopsDAOService.createLaptop(employeeId, laptop);
                return ResponseEntity.ok().body(HttpStatus.CREATED);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLaptopById(@PathVariable Long id) throws ResourceNotFoundException {
        if (id != null) {
            Laptop laptop = this.laptopsDAOService.getLaptopById(id);
            if (laptop == null) {
                throw new ResourceNotFoundException("laptop not found for this id: " + id);
            }
            return ResponseEntity.ok().body(laptop);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<?> updateLaptopById(@RequestBody Laptop laptopToUpdate) throws ResourceNotFoundException {
        Boolean valid = laptopToUpdate.validateSelf();
        if (laptopToUpdate.getId() != null && valid) {
            String response = this.laptopsDAOService.updateLaptopById(laptopToUpdate);
            if (response != null && response.equals("OK")) {
                return ResponseEntity.ok().body(HttpStatus.OK);
            }
            return ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLaptop(@PathVariable("id") Long id) throws Exception {
        if (id != null) {
            try {
                String response = this.laptopsDAOService.deleteLaptopById(id);
                return response != null && response.equals("OK")
                        ? ResponseEntity.ok().body(HttpStatus.OK)
                        : ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
}
