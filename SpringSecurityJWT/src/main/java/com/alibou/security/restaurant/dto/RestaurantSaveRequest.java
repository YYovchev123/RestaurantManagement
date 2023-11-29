package com.alibou.security.restaurant.dto;

import com.alibou.security.restaurant.model.RestaurantCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantSaveRequest {

    private String name;
    private String location;
    private String phone;
    private RestaurantCategory restaurantCategory;
}
