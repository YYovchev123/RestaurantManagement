package com.alibou.security.user.dto;

import com.alibou.security.restaurant.dto.RestaurantResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private List<RestaurantResponse> restaurant;
}
