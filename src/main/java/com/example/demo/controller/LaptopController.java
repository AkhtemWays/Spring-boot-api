package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Laptop;
import com.example.demo.repository.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/laptops")
public class LaptopController {
    @Autowired
    private LaptopRepository laptopRepository;

    @GetMapping
    public List<Laptop> getAllLaptops() {
        return this.laptopRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addLaptop(@RequestBody Laptop laptop) {
        Boolean valid = laptop.validateSelf();
        System.out.println(valid);
        if (valid) {
            this.laptopRepository.save(laptop);
            return ResponseEntity.ok().body(HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLaptopById(@PathVariable Long id) throws ResourceNotFoundException {
        if (id != null) {
            Laptop laptop = this.laptopRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("laptop not found for this id: " + id));
            return ResponseEntity.ok().body(laptop);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLaptopById(@PathVariable Long id,
                                              @RequestBody Laptop laptopToUpdate) throws ResourceNotFoundException {
        Boolean valid = laptopToUpdate.validateSelf();
        if (id != null && valid) {
            Laptop currentLaptop = this.laptopRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("laptop not found for this id: " + id));
            currentLaptop.setLaptopType(laptopToUpdate.getLaptopType());
            currentLaptop.setGraphicsCard(laptopToUpdate.getGraphicsCard());
            currentLaptop.setProcessor(laptopToUpdate.getProcessor());
            this.laptopRepository.save(currentLaptop);
            return ResponseEntity.ok().body(HttpStatus.ACCEPTED);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLaptopById(@PathVariable Long id) throws ResourceNotFoundException {
        if (id != null) {
            Laptop laptop = this.laptopRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Laptop not found for this id: " + id));
            this.laptopRepository.delete(laptop);
            return ResponseEntity.ok().body(HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
}
