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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        //given
        Restaurant restaurant = Restaurant.builder()
                .name("TestRestaurant")
                .phone("0888888888")
                .build();
        //when
        ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);
        restaurantService.save(restaurant);
        //then
        verify(restaurantRepository, times(1)).save(restaurantArgumentCaptor.capture());
        Restaurant capturedRestaurant = restaurantArgumentCaptor.getValue();
        assertThat(capturedRestaurant).isEqualTo(restaurant);
    }

    @Test
    public void verifyFindAll() {
        //when
        restaurantService.findAll();
        //then
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    public void verifyFindById() {
        //given
        long id = 1L;
        Restaurant restaurant = Restaurant.builder()
                .id(id)
                .name("TestRestaurant")
                .phone("0888888888")
                .build();
        //when
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        restaurantService.findById(id);
        //then
        verify(restaurantRepository, times(1)).findById(id);
        assertEquals(id, restaurant.getId());
    }

    @Test
    public void verifyDeleteById() {
        //given
        long id = 1L;
        Restaurant restaurant = Restaurant.builder()
                .id(id)
                .name("TestRestaurant")
                .phone("0888888888")
                .build();
        //when
        restaurantService.deleteById(id);
        //then
        verify(restaurantRepository, times(1)).deleteById(id);
    }

    @Test
    public void verifyAddFoodToMenu() {
        //given
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
        //when
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(foodService.findById(id)).thenReturn(food);
        restaurantService.addFoodToMenu(restaurant.getId(), food.getId());
        //then
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
