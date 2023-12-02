package com.alibou.security.user.converter;

import com.alibou.security.restaurant.converter.RestaurantConverter;
import com.alibou.security.user.dto.UserResponse;
import com.alibou.security.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final RestaurantConverter restaurantConverter;

    public UserResponse convert(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstname())
                .lastName(user.getFirstname())
                .email(user.getEmail())
                .restaurant(user.getRestaurants()
                        .stream()
                        .map(restaurantConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
