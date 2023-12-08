package com.alibou.security.food.service;

import com.alibou.security.exception.RecordNotFoundException;
import com.alibou.security.food.model.Food;
import com.alibou.security.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService{

    private final FoodRepository foodRepository;

    @Override
    public Food save(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    @Override
    public Food findById(long id) {
        return foodRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(String.format("Food with id: %s not found!", id)));
    }

    @Override
    public void deleteById(long id) {
        foodRepository.deleteById(id);
    }

    // Todo: make this function
    @Override
    public Food update(Food updatedFood, long id) {
        return null;
    }
}