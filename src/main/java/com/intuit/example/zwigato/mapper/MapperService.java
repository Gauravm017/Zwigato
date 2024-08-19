package com.intuit.example.zwigato.mapper;

import com.intuit.example.zwigato.dto.*;
import com.intuit.example.zwigato.exception.DataMappingException;
import com.intuit.example.zwigato.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapperService {

    public static OrderDto convertOrderToDto(Order order) {
        try {
            List<OrderItemDto> itemDTOs = new ArrayList<>();
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setItems(itemDTOs);
            orderDto.setStatus(order.getStatus());
            orderDto.setTotalBill(order.getTotalBill());
            orderDto.setItems(order.getItems().stream().map(MapperService::convertOrderItemToDto).collect(Collectors.toList()));
            return orderDto;
        }
        catch (Exception e){
            throw new DataMappingException("Issue in conversion of order dto");
        }
    }

    public static OrderItemDto convertOrderItemToDto(OrderItem orderItem){
        try {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setId(orderItem.getId());
            orderItemDto.setMenuItemName(orderItem.getMenuItem().getName());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setRestaurantName(orderItem.getRestaurant().getName());
            orderItemDto.setPrice(orderItem.getPrice());
            return orderItemDto;
        }
        catch (Exception e){
            throw new DataMappingException("Issue in conversion of order item dto");
        }
    }

    public static UserDto convertUserToDto(User user){
        try {
            UserDto userDto = new UserDto();

            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setName(user.getName());

            userDto.setOrders(user.getOrders().stream().map(MapperService::convertOrderToDto).collect(Collectors.toList()));
            return userDto;
        }
        catch (Exception e){
            throw new DataMappingException("Issue in conversion of User dto");
        }
    }

    public static MenuDto convertMenuToDto(MenuItem menuItem){
        try {
            MenuDto  menuDto = new MenuDto();

            menuDto.setId(menuItem.getId());
            menuDto.setName(menuItem.getName());
            return menuDto;
        }
        catch (Exception e){
            throw new DataMappingException("Issue in conversion of Menu dto");
        }
    }

    public static RestaurantItemDto convertRestaurantItemToDto(RestaurantMenuItem restaurantMenuItem){
        try {
            RestaurantItemDto restaurantItemDto = new RestaurantItemDto();

            restaurantItemDto.setId(restaurantMenuItem.getId());
            restaurantItemDto.setName(restaurantMenuItem.getMenuItem().getName());
            restaurantItemDto.setPrice(restaurantMenuItem.getPrice());
            return restaurantItemDto;
        }
        catch (Exception e){
            throw new DataMappingException("Issue in conversion of Restaurant item dto");
        }
    }

    public static RestaurantDto convertRestaurantToDto(Restaurant restaurant){
        try {
            RestaurantDto restaurantDto = new RestaurantDto();

            restaurantDto.setId(restaurant.getId());
            restaurantDto.setRating(restaurant.getRating());
            restaurantDto.setName(restaurant.getName());
            restaurantDto.setMaxCapacity(restaurant.getMaxCapacity());
            restaurantDto.setCurrentProcessingLoad(restaurant.getCurrentProcessingLoad());
            restaurantDto.setRestaurantItemDtoList(restaurant.getRestaurantMenuItem().stream().map(MapperService::convertRestaurantItemToDto).collect(Collectors.toList()));
            return restaurantDto;
        }
        catch (Exception e){
            throw new DataMappingException("Issue in conversion of Restaurant dto");
        }
    }


}
