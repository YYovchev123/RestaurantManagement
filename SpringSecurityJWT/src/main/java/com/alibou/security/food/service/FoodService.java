package com.alibou.security.food.service;

import com.alibou.security.food.model.Food;

import java.util.List;

public interface FoodService {

    Food save(Food food);

    List<Food> findAll();

    Food findById(long id);

    void deleteById(long id);

    Food update(Food updatedFood, long id);
}
