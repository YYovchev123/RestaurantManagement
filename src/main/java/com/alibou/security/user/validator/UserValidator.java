package com.alibou.security.user.validator;

import com.alibou.security.exception.RecordBadRequestException;
import com.alibou.security.food.model.Food;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.user.model.Role;
import com.alibou.security.user.model.User;
import com.alibou.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private  final UserService userService;

    public void isUserOwner(Restaurant restaurant, User user) {
        if(!user.getRestaurants().contains(restaurant) && !user.getRole().equals(Role.ADMIN)){
            throw new RecordBadRequestException("Not the owner!");
        }
    }

    public void isFoodInOwnerMenu(Food food, User user, Restaurant restaurant) {
        isUserOwner(restaurant, user);
        if(!restaurant.getMenu().contains(food)) {
           throw new RecordBadRequestException("Food not in the menu!");
        }
    }
}
