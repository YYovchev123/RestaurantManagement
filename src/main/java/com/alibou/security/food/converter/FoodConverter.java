package com.alibou.security.food.converter;

import com.alibou.security.food.dto.FoodResponse;
import com.alibou.security.food.dto.FoodSaveRequest;
import com.alibou.security.food.model.Food;
import org.springframework.stereotype.Component;

@Component
public class FoodConverter {

    public Food convert(FoodSaveRequest foodSaveRequest) {
        return Food.builder()
                .name(foodSaveRequest.getName())
                .image(foodSaveRequest.getImage())
                .price(foodSaveRequest.getPrice())
                .description(foodSaveRequest.getDescription())
                .foodType(foodSaveRequest.getFoodType())
                .foodCategory(foodSaveRequest.getFoodCategory())
                .ingredients(foodSaveRequest.getIngredients())
                .build();
    }

    public FoodResponse convert(Food food) {
        return FoodResponse.builder()
                .name(food.getName())
                .image(food.getImage())
                .price(food.getPrice())
                .description(food.getDescription())
                .foodType(food.getFoodType())
                .foodCategory(food.getFoodCategory())
                .ingredients(food.getIngredients())
                .build();
    }
}
