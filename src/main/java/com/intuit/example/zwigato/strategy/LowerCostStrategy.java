package com.intuit.example.zwigato.strategy;

import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.model.Restaurant;
import com.intuit.example.zwigato.model.RestaurantMenuItem;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LowerCostStrategy implements RestaurantSelectionStrategy {
    @Override
    public Restaurant selectRestaurant(MenuItem menuItem, int quantity){
        List<RestaurantMenuItem> availableItems = menuItem.getRestaurantMenuItem().stream()
                .filter(item -> {
                    Restaurant restaurant = item.getRestaurant();
                    return restaurant.getCurrentProcessingLoad() + quantity <= restaurant.getMaxCapacity();
                })
                .toList();
        return availableItems.stream()
                .min(Comparator.comparingDouble(RestaurantMenuItem::getPrice))
                .map(RestaurantMenuItem::getRestaurant)
                .orElse(null);
    }
}
