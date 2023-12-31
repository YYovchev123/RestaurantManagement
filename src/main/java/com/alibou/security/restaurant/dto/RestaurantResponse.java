package com.alibou.security.restaurant.dto;

import com.alibou.security.food.dto.FoodResponse;
import com.alibou.security.restaurant.model.RestaurantCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class RestaurantResponse {

    private String name;
    private String location;
    private String phone;
    private RestaurantCategory restaurantCategory;
    private List<FoodResponse> menu;
}
