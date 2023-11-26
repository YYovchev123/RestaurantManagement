package com.alibou.security.restaurant.service;

import com.alibou.security.food.model.Food;
import com.alibou.security.restaurant.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant save(Restaurant restaurant);

    List<Restaurant> findAll();

    Restaurant update(long id, Restaurant updatedRestaurant);

    Restaurant findById(long id);

    void deleteById(long id);

    Food addFoodToMenu(long restaurantId, long foodId);

    void removeFoodFromMenu(long restaurantId, long foodId);
}
