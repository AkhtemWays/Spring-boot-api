package com.example.demo.model;

import com.example.demo.shared.LaptopType;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "laptops")
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private LaptopType laptopType;

    @Column(name = "graphics_card")
    private String graphicsCard;

    @Column(name = "processor")
    private String processor;

    public Long getId() {
        return id;
    }

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Employee.class)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employeeId;

    public Laptop() {super();}

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employee) {
        this.employeeId = employee;
    }

    public Laptop(LaptopType laptopType, String graphicsCard, String processor, Employee employee) {
        this.laptopType = laptopType;
        this.graphicsCard = graphicsCard;
        this.processor = processor;
        this.employeeId = employee;
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "id=" + id +
                ", laptopType=" + laptopType +
                ", graphicsCard='" + graphicsCard + '\'' +
                ", processor='" + processor + '\'' +
                ", employeeId=" + employeeId +
                '}';
    }

    public Boolean validateSelf() {
        Boolean isGraphicsCard = this.getGraphicsCard() != null && !this.getGraphicsCard().equals("");
        Boolean isProcessor = this.getProcessor() != null && !this.getProcessor().equals("");
        Boolean isLaptopType = LaptopType.ACER.validateLaptopType(this.getLaptopType());
        return isGraphicsCard && isLaptopType && isProcessor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LaptopType getLaptopType() {
        return laptopType;
    }

    public void setLaptopType(LaptopType laptopType) {
        this.laptopType = laptopType;
    }

    public String getGraphicsCard() {
        return graphicsCard;
    }

    public void setGraphicsCard(String graphicsCard) {
        this.graphicsCard = graphicsCard;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }
}
