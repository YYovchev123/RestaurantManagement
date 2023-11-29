package com.alibou.security.restaurant.controller;

import com.alibou.security.config.JwtService;
import com.alibou.security.restaurant.converter.RestaurantConverter;
import com.alibou.security.restaurant.dto.RestaurantResponse;
import com.alibou.security.restaurant.dto.RestaurantSaveRequest;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.restaurant.service.RestaurantService;
import com.alibou.security.user.model.User;
import com.alibou.security.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> save(@RequestBody RestaurantSaveRequest restaurantSaveRequest) {
        Restaurant restaurant = restaurantConverter.convert(restaurantSaveRequest);
        Restaurant savedRestaurant = restaurantService.save(restaurant);
        RestaurantResponse restaurantResponse = restaurantConverter.convert(savedRestaurant);
        return ResponseEntity.ok(restaurantResponse);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        List<Restaurant> restaurants = restaurantService.findAll();
        return ResponseEntity.ok(restaurants.stream().map(restaurantConverter::convert).collect(Collectors.toList()));
    }

    // TODO: make it so only the owner of the restaurant can get his restaurants. Non owners cannot access restaurants. Admins can access all restaurants
    @GetMapping(value = "/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable long id) {
        Restaurant restaurant = restaurantService.findById(id);
        RestaurantResponse restaurantResponse = restaurantConverter.convert(restaurant);
        return ResponseEntity.ok(restaurantResponse);
    }

    @GetMapping("/endpoint")
    public String getTokenFromHeader(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract token from Authorization header
        String token = authorizationHeader.replace("Bearer ", ""); // Assuming a Bearer token
        String email = jwtService.extractUsername(token);
        // Your logic here with the retrieved token
        return "Token: " + token + " " + email;
    }
}