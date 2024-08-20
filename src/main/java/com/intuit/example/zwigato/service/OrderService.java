package com.intuit.example.zwigato.service;

import com.intuit.example.zwigato.exception.OrderProcessingException;
import com.intuit.example.zwigato.model.*;
import com.intuit.example.zwigato.repository.MenuItemRepository;
import com.intuit.example.zwigato.repository.OrderRepository;
import com.intuit.example.zwigato.repository.RestaurantRepository;
import com.intuit.example.zwigato.strategy.RestaurantFilterInterface;
import com.intuit.example.zwigato.strategy.RestaurantSelectionStrategy;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private RestaurantSelectionStrategy restaurantSelectionStrategy;

    @Autowired
    private List<RestaurantFilterInterface> filterServices;

    @Autowired
    private OrderProcessingService orderProcessingService;

    @Autowired
    private UserService userService;

    @Transactional
    public Order placeOrder(Long userId, Map<Long, Integer> items) {
        Lock lock = new ReentrantLock();
        try {
            //Extracting the user
            User user = userService.getUserById(userId);
            if(Objects.nonNull(user)){
                //Extracting all the menu items
                List<MenuItem> menuItems = menuItemRepository.findAllById(items.keySet());

                //Checking the order if possible
                processFilters(menuItems);

                List<Restaurant> selectedRestaurants = new ArrayList<>();

                try {
                    lock.lock();
                    //Create order
                    Order order = createOrder(menuItems, items, selectedRestaurants);

                    List<Order> orderList = user.getOrders();
                    if (CollectionUtils.isEmpty(orderList)) {
                        orderList = new ArrayList<>();
                    }
                    orderList.add(order);

                    user.setOrders(orderList);

                    order.setUser(user);

                    restaurantRepository.saveAll(selectedRestaurants);
                    Order savedOrder = orderRepository.save(order);
                    orderProcessingService.prepareOrder(savedOrder);
                    return savedOrder;
                }
                finally {
                    lock.unlock();
                }

            }else{
                throw new RuntimeException("No User Found");
            }
        } catch (Exception e) {
           log.error("error in placing the order", e);
            throw e;
        }
    }

    public void processFilters(List<MenuItem> menuItems){
        List<RestaurantMenuItem> restaurantMenuItems = menuItems.stream()
                .flatMap(menuItem -> menuItem.getRestaurantMenuItem().stream()).toList();

        Set<Restaurant> restaurantSet = new HashSet<>();
        for (RestaurantMenuItem restaurantMenuItem : restaurantMenuItems) {
            Restaurant restaurant = restaurantMenuItem.getRestaurant();
            restaurantSet.add(restaurant);
        }
        filterServices.stream().filter(x -> x.order() >= 0).sorted(Comparator.comparingInt(RestaurantFilterInterface::order)).toList().forEach(
                filterService -> {
                    if (filterService.isFilterApplicable(menuItems, restaurantSet.stream().toList())) {
                        if(!filterService.checkForCondition(menuItems, restaurantSet.stream().toList())){
                            throw new OrderProcessingException("Unable to process the order");
                        }
                    }
                });
    }

    public Order createOrder(List<MenuItem> menuItems, Map<Long, Integer> items, List<Restaurant> selectedRestaurants) {

        //Creating new order
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setTotalBill(0.0);

        List<OrderItem> orderItems = new ArrayList<>(items.size());

        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            int quantity = entry.getValue();

            MenuItem menuItem = menuItems.stream().filter(menuItem1 -> {
                return Objects.equals(menuItem1.getId(), entry.getKey());
            }).findFirst().orElse(null);

            // Implement selection strategy (e.g., lower cost)
            Restaurant selectedRestaurant = restaurantSelectionStrategy.selectRestaurant(menuItem, entry.getValue());

            RestaurantMenuItem restaurantMenuItem = selectedRestaurant.getRestaurantMenuItem().stream()
                    .filter(restaurantMenuItem1 -> restaurantMenuItem1.getMenuItem().equals(menuItem))
                    .findFirst()
                    .orElse(null);

            if (selectedRestaurant != null && restaurantMenuItem != null) {
                selectedRestaurant.setCurrentProcessingLoad(selectedRestaurant.getCurrentProcessingLoad() + quantity);
                selectedRestaurants.add(selectedRestaurant);

                OrderItem orderItem = new OrderItem();
                orderItem.setRestaurant(selectedRestaurant);
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(quantity);
                orderItem.setOrder(order);
                orderItem.setPrice(restaurantMenuItem.getPrice());
                orderItems.add(orderItem);

                order.setTotalBill(order.getTotalBill()+restaurantMenuItem.getPrice());
                }
        }
        order.setItems(orderItems);
        return order;
    }
}

