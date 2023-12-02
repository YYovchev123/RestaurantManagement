package com.alibou.security.food.controller;

import com.alibou.security.food.converter.FoodConverter;
import com.alibou.security.food.dto.FoodResponse;
import com.alibou.security.food.dto.FoodSaveRequest;
import com.alibou.security.food.model.Food;
import com.alibou.security.food.service.FoodService;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.restaurant.service.RestaurantService;
import com.alibou.security.user.model.User;
import com.alibou.security.user.service.UserService;
import com.alibou.security.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodConverter foodConverter;
    private final FoodService foodService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final UserValidator userValidator;

    //Todo: test this method
    @PostMapping(value = "/{rid}")
    public ResponseEntity<FoodResponse> saveToMenu(@PathVariable long rid, @RequestBody FoodSaveRequest foodSaveRequest,
                                                   @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getEmailFromToken(authorizationHeader);
        Restaurant restaurant = restaurantService.findById(rid);
        userValidator.isUserOwner(restaurant, user);
        Food food = save(foodSaveRequest);
        restaurantService.addFoodToMenu(restaurant.getId(), food.getId());
        return ResponseEntity.ok(foodConverter.convert(food));
    }

    //Todo: Make it so only ADMINS and the owner of the restaurant can access this method
    @GetMapping(value = "/menu/{rid}")
    public ResponseEntity<List<FoodResponse>> getMenuForRestaurant(@PathVariable long rid,
                                                                   @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getEmailFromToken(authorizationHeader);
        Restaurant restaurant = restaurantService.findById(rid);
        userValidator.isUserOwner(restaurant, user);
        List<Food> menu = restaurant.getMenu();
        return ResponseEntity.ok(menu.stream().map(foodConverter::convert).collect(Collectors.toList()));
    }

    //Todo: secure endpoint
    @GetMapping
    public ResponseEntity<List<FoodResponse>> findAll() {
        List<Food> foods = foodService.findAll();
        return ResponseEntity.ok(foods.stream().map(foodConverter::convert).collect(Collectors.toList()));
    }

    //Todo: secure endpoint
    @GetMapping(value = "/{id}")
    public ResponseEntity<FoodResponse> findById(@PathVariable long id) {
        Food food = foodService.findById(id);
        FoodResponse foodResponse = foodConverter.convert(food);
        return ResponseEntity.ok(foodResponse);
    }

    //Todo: secure endpoint
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable long id) {
        foodService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Food save(FoodSaveRequest foodSaveRequest) {
        Food food = foodConverter.convert(foodSaveRequest);
        Food savedFood = foodService.save(food);
        return savedFood;
    }
}


