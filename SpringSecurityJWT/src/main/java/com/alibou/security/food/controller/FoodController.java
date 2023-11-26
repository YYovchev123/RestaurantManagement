package com.alibou.security.food.controller;

import com.alibou.security.food.converter.FoodConverter;
import com.alibou.security.food.dto.FoodResponse;
import com.alibou.security.food.dto.FoodSaveRequest;
import com.alibou.security.food.model.Food;
import com.alibou.security.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodConverter foodConverter;
    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodResponse> save(@RequestBody FoodSaveRequest foodSaveRequest) {
        Food food = foodConverter.convert(foodSaveRequest);
        Food savedFood = foodService.save(food);
        FoodResponse foodResponse = foodConverter.convert(savedFood);
        return ResponseEntity.ok(foodResponse);
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> findAll() {
        List<Food> foods = foodService.findAll();
        return ResponseEntity.ok(foods.stream().map(foodConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FoodResponse> findById(@PathVariable long id) {
        Food food = foodService.findById(id);
        FoodResponse foodResponse = foodConverter.convert(food);
        return ResponseEntity.ok(foodResponse);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable long id) {
        foodService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
