package com.alibou.security.food.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String image;
    @NotNull
    private float price;
    @NotNull
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FoodType foodType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;
    @NotNull
    private List<String> ingredients;
}
