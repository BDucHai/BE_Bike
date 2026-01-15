package com.example.Dto;

import com.example.entity.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BikeDto {

        private Long id;

        private String name;

        private String color;

        private String brand;

        private String model;

        private Long price;

        private String state;

        private String manufactureYear;

        private String otherTech;

        private Float traveledKm;

        private String machineCondition;

        private String description;

        private String advantage;

        private Integer maxSpeed;

        private Integer hot;

}
