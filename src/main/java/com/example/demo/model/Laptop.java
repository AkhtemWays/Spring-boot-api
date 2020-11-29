package com.example.demo.model;

import com.example.demo.shared.LaptopType;

import javax.persistence.*;

@Entity
@Table(name = "laptops")
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private LaptopType laptopType;

    @Column(name = "graphics_card")
    private String graphicsCard;

    @Column(name = "processor")
    private String processor;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "employee_id", nullable = false, unique = false, referencedColumnName = "id")
    private Employee employeeId;

    public Laptop() {super();}

    public Laptop(LaptopType laptopType, String graphicsCard, String processor, Employee employeeId) {
        this.laptopType = laptopType;
        this.graphicsCard = graphicsCard;
        this.processor = processor;
        this.employeeId = employeeId;
    }

    public Boolean validateSelf() {
        Boolean isGraphicsCard = Boolean.parseBoolean(this.getGraphicsCard());
        Boolean isProcessor = Boolean.parseBoolean(this.getProcessor());
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
