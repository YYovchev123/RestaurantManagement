package com.alibou.security.restaurant.converter;

import com.alibou.security.food.converter.FoodConverter;
import com.alibou.security.restaurant.dto.RestaurantResponse;
import com.alibou.security.restaurant.dto.RestaurantSaveRequest;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.user.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantConverter {

    private final FoodConverter foodConverter;

    public Restaurant convert(RestaurantSaveRequest restaurantSaveRequest) {

        return Restaurant.builder()
                .name(restaurantSaveRequest.getName())
                .location(restaurantSaveRequest.getLocation())
                .phone(restaurantSaveRequest.getPhone())
                .restaurantCategory(restaurantSaveRequest.getRestaurantCategory())
                .build();
    }

    public RestaurantResponse convert(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .name(restaurant.getName())
                .location(restaurant.getLocation())
                .phone(restaurant.getPhone())
                .restaurantCategory(restaurant.getRestaurantCategory())
                .menu(Optional
                        .ofNullable(restaurant.getMenu())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(foodConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
