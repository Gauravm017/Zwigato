package com.intuit.example.zwigato.strategy;

import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.model.Restaurant;

import java.util.List;

public interface RestaurantSelectionStrategy {
    Restaurant selectRestaurant(MenuItem menuItem, int quantity);
}
