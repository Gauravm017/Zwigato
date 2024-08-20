package com.intuit.example.zwigato.service;

import com.intuit.example.zwigato.model.Order;
import com.intuit.example.zwigato.model.OrderStatus;
import com.intuit.example.zwigato.model.Restaurant;
import com.intuit.example.zwigato.repository.OrderRepository;
import com.intuit.example.zwigato.repository.RestaurantRepository;
import com.intuit.example.zwigato.utility.ThreadConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProcessingService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public void prepareOrder(Order order){
        ThreadConfigManager.runAsync(order.getId().toString(), ()->{
            try {
                Thread.sleep(15000);
                order.setStatus(OrderStatus.PREPARE);
                Order savedOrder = orderRepository.save(order);
                dispatchOrder(savedOrder);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void dispatchOrder(Order order){
        ThreadConfigManager.runAsync(order.getId().toString(), ()->{
            try {
                Thread.sleep(15000);
                order.setStatus(OrderStatus.DISPATCHED);
                Order savedOrder = orderRepository.save(order);
                order.getItems().forEach(orderItem -> {
                    Restaurant restaurant = orderItem.getRestaurant();
                    restaurant.setCurrentProcessingLoad(restaurant.getCurrentProcessingLoad()-orderItem.getQuantity());
                    restaurantRepository.save(restaurant);
                });
                completeOrder(savedOrder);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void completeOrder(Order order){
        ThreadConfigManager.runAsync(order.getId().toString(), ()->{
            try {
                Thread.sleep(15000);
                order.setStatus(OrderStatus.COMPLETED);
                Order savedOrder = orderRepository.save(order);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
