package com.alibou.security.service;

import com.alibou.security.exception.RecordNotFoundException;
import com.alibou.security.food.model.Food;
import com.alibou.security.food.service.FoodService;
import com.alibou.security.restaurant.model.Restaurant;
import com.alibou.security.restaurant.repository.RestaurantRepository;
import com.alibou.security.restaurant.service.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private FoodService foodService;

    @BeforeEach
    public void setUp() {
        restaurantService = new RestaurantServiceImpl(restaurantRepository, foodService);
    }

    @Test
    public void verifySave() {
        Restaurant restaurant = Restaurant.builder()
                .name("TestRestaurant")
                .phone("0888888888")
                .build();

        restaurantService.save(restaurant);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    public void verifyFindAll() {
        restaurantService.findAll();
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    public void verifyFindById() {
        long id = 1L;
        Restaurant restaurant = Restaurant.builder()
                .id(id)
                .name("TestRestaurant")
                .phone("0888888888")
                .build();
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        restaurantService.findById(id);
        verify(restaurantRepository, times(1)).findById(id);
        assertEquals(id, restaurant.getId());
    }

    @Test
    public void verifyDeleteById() {
        long id = 1L;
        Restaurant restaurant = Restaurant.builder()
                .id(id)
                .name("TestRestaurant")
                .phone("0888888888")
                .build();
        restaurantService.deleteById(id);
        verify(restaurantRepository, times(1)).deleteById(id);
    }

    @Test
    public void verifyAddFoodToMenu() {
        long id = 1L;
        Restaurant restaurant = Restaurant.builder()
                .id(id)
                .name("TestRestaurant")
                .phone("0888888888")
                .menu(new ArrayList<>())
                .build();
        Food food = Food.builder()
                .id(id)
                .name("Salad")
                .build();
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(foodService.findById(id)).thenReturn(food);
        restaurantService.addFoodToMenu(restaurant.getId(), food.getId());
        boolean isTest = restaurant.getMenu().size() == 1;
        assertTrue(isTest);
    }

    @Test
    public void verifyFindByIdThrowsException() {
        String exceptionMessage = "Restaurant with id: 1 not found!";
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            restaurantService.findById(1L);
        });
        assertEquals(exceptionMessage, exception.getMessage());
    }
}
