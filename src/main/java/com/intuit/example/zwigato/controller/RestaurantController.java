package com.intuit.example.zwigato.controller;

import com.intuit.example.zwigato.dto.RestaurantDto;
import com.intuit.example.zwigato.mapper.MapperService;
import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.model.Restaurant;
import com.intuit.example.zwigato.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/register")
    public ResponseEntity<RestaurantDto> registerRestaurant(@RequestBody Restaurant restaurant) {
        RestaurantDto restaurantDto = MapperService.convertRestaurantToDto(restaurantService.registerRestaurant(restaurant));
        return new ResponseEntity<>(restaurantDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        List<Restaurant> restaurantList = restaurantService.getAllRestaurants();
        List<RestaurantDto> restaurantDtoList = restaurantList.stream().map(MapperService::convertRestaurantToDto).toList();
        return new ResponseEntity<>(restaurantDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteRestaurant(@RequestParam Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{restaurantId}/menu/update")
    public ResponseEntity<Void> updateMenu(@PathVariable Long restaurantId, @RequestBody Map<String,Double> menuItems) {
        restaurantService.updateMenu(restaurantId, menuItems);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
