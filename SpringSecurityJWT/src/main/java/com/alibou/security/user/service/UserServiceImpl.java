package com.alibou.security.user.service;

import com.alibou.security.exception.RecordNotFoundException;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.user.model.User;
import com.alibou.security.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Todo: make this function
    @Override
    public User update(long id, User updatedUser) {
        return null;
    }

    // Todo: make this function
    @Override
    public void resetPassword(String newPassword, String currentPassword, String email) {

    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(String.format("User with id: %s not found!", id)));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException(String.format("User with email: %s not found!", email)));
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setUser(String email, Restaurant restaurant) {
        User user = findByEmail(email);
        restaurant.setOwner(user);
    }
}
