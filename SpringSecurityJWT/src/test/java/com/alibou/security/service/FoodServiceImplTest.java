package com.alibou.security.service;

import com.alibou.security.exception.RecordNotFoundException;
import com.alibou.security.food.model.Food;
import com.alibou.security.food.repository.FoodRepository;
import com.alibou.security.food.service.FoodService;
import com.alibou.security.food.service.FoodServiceImpl;
import com.alibou.security.restaurant.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodServiceImplTest {

    @InjectMocks
    private FoodServiceImpl foodService;
    @Mock
    private FoodRepository foodRepository;

    @BeforeEach
    public void setUp() {
        foodService = new FoodServiceImpl(foodRepository);
    }

    @Test
    public void verifySave() {
        Food food = Food.builder()
                .name("TestFood")
                .build();

        foodService.save(food);
        verify(foodRepository, times(1)).save(food);
    }

    @Test
    public void verifyFindAll() {
        foodService.findAll();
        verify(foodRepository, times(1)).findAll();
    }

    @Test
    public void verifyFindById() {
        long id = 1L;
        Food food = Food.builder()
                .id(id)
                .name("TestFood")
                .build();
        when(foodRepository.findById(id)).thenReturn(Optional.of(food));
        foodService.findById(id);
        verify(foodRepository, times(1)).findById(id);
        assertEquals(id, food.getId());
    }

    @Test
    public void verifyDeleteById() {
        long id = 1L;
        Food food = Food.builder()
                .id(id)
                .name("TestFood")
                .build();
        foodService.deleteById(id);
        verify(foodRepository, times(1)).deleteById(id);
    }

    @Test
    public void verifyFindByIdThrowsException() {
        String exceptionMessage = "Food with id: 1 not found!";
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            foodService.findById(1L);
        });
        assertEquals(exceptionMessage, exception.getMessage());
    }
}
