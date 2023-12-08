package com.alibou.security.restaurant.model;

import com.alibou.security.food.model.Food;
import com.alibou.security.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private String location;
    private String phone;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "foods_id")
    private List<Food> menu;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
