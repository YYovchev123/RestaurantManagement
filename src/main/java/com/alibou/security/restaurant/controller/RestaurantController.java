package com.alibou.security.restaurant.controller;

import com.alibou.security.restaurant.converter.RestaurantConverter;
import com.alibou.security.restaurant.dto.RestaurantResponse;
import com.alibou.security.restaurant.dto.RestaurantSaveRequest;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.restaurant.service.RestaurantService;
import com.alibou.security.user.model.User;
import com.alibou.security.user.service.UserService;
import com.alibou.security.user.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantConverter restaurantConverter;
    private final UserService userService;
    private final UserValidator userValidator;

    @PostMapping
    @Transactional
    public ResponseEntity<RestaurantResponse> save(@RequestBody RestaurantSaveRequest restaurantSaveRequest, @RequestHeader("Authorization") String authorizationHeader) {
        try {
        User user = userService.getEmailFromToken(authorizationHeader);
        Restaurant restaurant = restaurantConverter.convert(restaurantSaveRequest);
        Restaurant savedRestaurant = restaurantService.save(restaurant);
        savedRestaurant.setOwner(user);
        RestaurantResponse restaurantResponse = restaurantConverter.convert(savedRestaurant);
        return ResponseEntity.ok(restaurantResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        List<Restaurant> restaurants = restaurantService.findAll();
        return ResponseEntity.ok(restaurants.stream().map(restaurantConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<RestaurantResponse>> getAllForUser(@RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getEmailFromToken(authorizationHeader);
        List<Restaurant> restaurants = user.getRestaurants();
        return ResponseEntity.ok(restaurants.stream().map(restaurantConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable long id, @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getEmailFromToken(authorizationHeader);
        Restaurant restaurant = restaurantService.findById(id);
        userValidator.isUserOwner(restaurant, user);
        RestaurantResponse restaurantResponse = restaurantConverter.convert(restaurant);
        return ResponseEntity.ok(restaurantResponse);
    }
}