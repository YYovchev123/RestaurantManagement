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
import com.alibou.security.user.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/menu/{rid}")
    public ResponseEntity<List<FoodResponse>> getMenuForRestaurant(@PathVariable long rid,
                                                                   @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getEmailFromToken(authorizationHeader);
        Restaurant restaurant = restaurantService.findById(rid);
        userValidator.isUserOwner(restaurant, user);
        List<Food> menu = restaurant.getMenu();
        return ResponseEntity.ok(menu.stream().map(foodConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> findAll() {
        List<Food> foods = foodService.findAll();
        return ResponseEntity.ok(foods.stream().map(foodConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "/{rid}/{id}")
    public ResponseEntity<FoodResponse> findById(@PathVariable long id, @PathVariable long rid, @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getEmailFromToken(authorizationHeader);
        Food food = foodService.findById(id);
        Restaurant restaurant = restaurantService.findById(rid);
        userValidator.isFoodInOwnerMenu(food, user, restaurant);
        FoodResponse foodResponse = foodConverter.convert(food);
        return ResponseEntity.ok(foodResponse);
    }

    @DeleteMapping(value = "/{rid}/{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteFromMenuById(@PathVariable long id, @PathVariable long rid,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getEmailFromToken(authorizationHeader);
        Food food = foodService.findById(id);
        Restaurant restaurant = restaurantService.findById(rid);
        userValidator.isFoodInOwnerMenu(food, user, restaurant);
        restaurantService.removeFoodFromMenu(rid, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Food save(FoodSaveRequest foodSaveRequest) {
        Food food = foodConverter.convert(foodSaveRequest);
        Food savedFood = foodService.save(food);
        return savedFood;
    }
}


