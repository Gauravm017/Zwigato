package com.intuit.example.zwigato.service;

import com.intuit.example.zwigato.exception.DatabaseOperationException;
import com.intuit.example.zwigato.exception.ResourceNotFoundException;
import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.model.Restaurant;
import com.intuit.example.zwigato.model.RestaurantMenuItem;
import com.intuit.example.zwigato.repository.MenuItemRepository;
import com.intuit.example.zwigato.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Transactional
    public Restaurant registerRestaurant(Restaurant restaurant) {
        try {
            restaurant.setRating(0.0);
            return restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to register restaurant", e);
        }
    }

    public List<Restaurant> getAllRestaurants(){
        try {
            return restaurantRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to register restaurant", e);
        }
    }

    public void deleteRestaurant(Long id){
        try {
            restaurantRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete restaurant", e);
        }
    }


    @Transactional
    public void updateMenu(Long restaurantId, Map<String,Double> menuItems) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

            List<RestaurantMenuItem> restaurantMenuItems = restaurant.getRestaurantMenuItem();
            if(restaurantMenuItems.isEmpty()){
                restaurantMenuItems = new ArrayList<>();
            }
            //List<MenuItem> menuItems = new ArrayList<>();
            for(Map.Entry<String, Double> entry : menuItems.entrySet()){
                MenuItem menuItem = menuItemRepository.findByName(entry.getKey());
                if(Objects.isNull(menuItem)){
                    menuItem = new MenuItem(entry.getKey());
                    menuItemRepository.save(menuItem);
                }
                RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
                restaurantMenuItem.setRestaurant(restaurant);
                restaurantMenuItem.setMenuItem(menuItem);
                restaurantMenuItem.setPrice(entry.getValue());
                restaurantMenuItems.add(restaurantMenuItem);
            }
            restaurant.setRestaurantMenuItem(restaurantMenuItems);
            restaurantRepository.save(restaurant);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update menu", e);
        }
    }
}
