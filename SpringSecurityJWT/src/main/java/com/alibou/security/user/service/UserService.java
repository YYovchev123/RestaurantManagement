package com.alibou.security.user.service;

import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.user.model.User;

import java.util.List;

public interface UserService {

    User save(User user);

    List<User> findAll();

    User update(long id, User updatedUser);

    void resetPassword(String newPassword, String currentPassword, String email);

    User findById(long id);

    User findByEmail(String email);

    void deleteById(long id);

    void setUser(String email, Restaurant restaurant);
}
