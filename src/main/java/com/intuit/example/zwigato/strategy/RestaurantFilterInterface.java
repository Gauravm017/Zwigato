package com.intuit.example.zwigato.strategy;

import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.model.Restaurant;

import java.util.List;
import java.util.Map;

public interface RestaurantFilterInterface {

    boolean isFilterApplicable(List<MenuItem> menuItems, List<Restaurant> restaurants);

    public int order();

    public boolean checkForCondition(List<MenuItem> menuItems, List<Restaurant> restaurants);
}
