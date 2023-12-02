package com.alibou.security.restaurant.service;

import com.alibou.security.exception.RecordNotFoundException;
import com.alibou.security.food.model.Food;
import com.alibou.security.food.service.FoodService;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final FoodService foodService;

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    //Todo: make this function
    @Override
    public Restaurant update(long id, Restaurant updatedRestaurant) {
        return null;
    }

    @Override
    public Restaurant findById(long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(String.format("Restaurant with id: %s not found!", id)));
    }

    @Override
    public void deleteById(long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Food addFoodToMenu(long restaurantId, long foodId) {
        Restaurant restaurant = findById(restaurantId);
        Food food = foodService.findById(foodId);
        restaurant.getMenu().add(food);
        return food;
    }

    //Todo: make this function
    @Override
    public void removeFoodFromMenu(long restaurantId, long foodId) {

    }
}
