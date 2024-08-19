package com.intuit.example.zwigato.strategy;

import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemsAvailableFilter implements RestaurantFilterInterface{
    @Override
    public boolean isFilterApplicable(List<MenuItem> menuItems, List<Restaurant> restaurants) {
        if(menuItems.size() > 0 && restaurants.size() > 0) return true;
        return false;
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public boolean checkForCondition(List<MenuItem> menuItems, List<Restaurant> restaurants) {
        for(MenuItem menuItem: menuItems){
            if (menuItem.getRestaurantMenuItem().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
