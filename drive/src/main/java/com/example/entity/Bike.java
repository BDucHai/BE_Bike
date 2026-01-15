package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Bike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "state")
    private String state;

    @Column(name = "manufactureYear")
    private String manufactureYear;

    @Column(name = "otherTech")
    private String otherTech;

    @Column(name = "traveledKm")
    private Float traveledKm;

    @Column(name = "machineCondition")
    private String machineCondition;

    @Column(name = "description")
    private String description;

    @Column(name = "advantage")
    private String advantage;

    @Column(name = "maxSpeed")
    private Integer maxSpeed;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "hot")
    private Integer hot;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public Bike() {
    }

    public Bike(String name, String color, String brand, String model, Long price, String state, String manufactureYear, String otherTech, Float traveledKm, String machineCondition, String description, String advantage, Integer maxSpeed, String avatar, LocalDateTime updatedAt, LocalDateTime createdAt, int hot) {
        this.name = name;
        this.color = color;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.state = state;
        this.manufactureYear = manufactureYear;
        this.otherTech = otherTech;
        this.traveledKm = traveledKm;
        this.machineCondition = machineCondition;
        this.description = description;
        this.advantage = advantage;
        this.maxSpeed = maxSpeed;
        this.avatar = avatar;
        this.updatedAt= updatedAt;
        this.createdAt = createdAt;
        this.hot = hot;
    }

}
