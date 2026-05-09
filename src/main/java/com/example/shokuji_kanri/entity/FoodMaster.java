package com.example.shokuji_kanri.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_masters")
@Data
public class FoodMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_name", nullable = false, length = 100)
    private String foodName;

    @Column(name = "calories")
    private Double calories;

    @Column(name = "protein_g")
    private Double proteinG;

    @Column(name = "fat_g")
    private Double fatG;

    @Column(name = "carbs_g")
    private Double carbsG;

    @Column(name = "serving_size_g")
    private Double servingSizeG;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}