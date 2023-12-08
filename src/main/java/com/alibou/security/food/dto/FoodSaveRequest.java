package com.alibou.security.food.dto;

import com.alibou.security.food.model.FoodCategory;
import com.alibou.security.food.model.FoodType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FoodSaveRequest {

    private String name;
    private String image;
    private float price;
    private String description;
    private FoodType foodType;
    private FoodCategory foodCategory;
    private List<String> ingredients;
}
