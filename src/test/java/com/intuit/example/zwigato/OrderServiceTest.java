package com.intuit.example.zwigato;

import com.intuit.example.zwigato.exception.OrderProcessingException;
import com.intuit.example.zwigato.model.*;
import com.intuit.example.zwigato.repository.MenuItemRepository;
import com.intuit.example.zwigato.repository.OrderRepository;
import com.intuit.example.zwigato.repository.RestaurantRepository;
import com.intuit.example.zwigato.service.OrderProcessingService;
import com.intuit.example.zwigato.service.OrderService;
import com.intuit.example.zwigato.service.UserService;
import com.intuit.example.zwigato.strategy.RestaurantFilterInterface;
import com.intuit.example.zwigato.strategy.RestaurantSelectionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantSelectionStrategy restaurantSelectionStrategy;

    @Mock
    private List<RestaurantFilterInterface> filterServices;

    @Mock
    private OrderProcessingService orderProcessingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_successful() {
        // Arrange
        Long userId = 1L;
        Map<Long, Integer> items = new HashMap<>();
        items.put(1L, 2);

        User user = new User();
        user.setId(userId);
        when(userService.getUserById(userId)).thenReturn(user);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setRestaurant(restaurant);
        restaurantMenuItem.setPrice(100.0);

        restaurant.setRestaurantMenuItem(Collections.singletonList(restaurantMenuItem));
        menuItem.setRestaurantMenuItem(Collections.singleton(restaurantMenuItem));
        restaurantMenuItem.setMenuItem(menuItem);
        restaurantMenuItem.setRestaurant(restaurant);

        when(menuItemRepository.findAllById(items.keySet())).thenReturn(Collections.singletonList(menuItem));
        when(restaurantSelectionStrategy.selectRestaurant(any(), anyInt())).thenReturn(restaurant);
        //when(restaurant.getRestaurantMenuItem()).thenReturn(Collections.singletonList(restaurantMenuItem));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order order = orderService.placeOrder(userId, items);

        // Assert
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        assertEquals(200.0, order.getTotalBill());
        verify(orderRepository, times(1)).save(order);
        verify(restaurantRepository, times(1)).saveAll(anyList());
        verify(orderProcessingService, times(1)).prepareOrder(order);
    }

    @Test
    void testPlaceOrder_userNotFound() {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(Exception.class, () -> orderService.placeOrder(userId, new HashMap<>()));
    }

    @Test
    void testProcessFilters_successful() {
        // Arrange
        MenuItem menuItem = new MenuItem();
        Restaurant restaurant = new Restaurant();

        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setRestaurant(restaurant);
        menuItem.setRestaurantMenuItem(Set.of(restaurantMenuItem));

        RestaurantFilterInterface filterInterface = mock(RestaurantFilterInterface.class);
        when(filterInterface.order()).thenReturn(1);
        when(filterInterface.isFilterApplicable(anyList(), anyList())).thenReturn(true);
        when(filterInterface.checkForCondition(anyList(), anyList())).thenReturn(true);
        when(filterServices.stream()).thenReturn(Stream.of(filterInterface));

        // Act
        orderService.processFilters(Collections.singletonList(menuItem));

        // Assert
        verify(filterInterface, times(1)).checkForCondition(anyList(), anyList());
    }

    @Test
    void testProcessFilters_throwsException() {
        // Arrange
        MenuItem menuItem = new MenuItem();
        Restaurant restaurant = new Restaurant();

        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setRestaurant(restaurant);
        menuItem.setRestaurantMenuItem(Set.of(restaurantMenuItem));

        RestaurantFilterInterface filterInterface = mock(RestaurantFilterInterface.class);
        when(filterInterface.order()).thenReturn(1);
        when(filterInterface.isFilterApplicable(anyList(), anyList())).thenReturn(true);
        when(filterInterface.checkForCondition(anyList(), anyList())).thenReturn(false);
        when(filterServices.stream()).thenReturn(Stream.of(filterInterface));

        // Act & Assert
        assertThrows(OrderProcessingException.class, () -> orderService.processFilters(Collections.singletonList(menuItem)));
    }

    @Test
    void testCreateOrder_successful() {
        // Arrange
        List<MenuItem> menuItems = new ArrayList<>();
        Map<Long, Integer> items = new HashMap<>();
        List<Restaurant> selectedRestaurants = new ArrayList<>();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItems.add(menuItem);

        Restaurant restaurant = new Restaurant();
        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setMenuItem(menuItem);
        restaurantMenuItem.setPrice(100.0);
        restaurant.setRestaurantMenuItem(Collections.singletonList(restaurantMenuItem));

        when(restaurantSelectionStrategy.selectRestaurant(any(), anyInt())).thenReturn(restaurant);

        // Act
        Order order = orderService.createOrder(menuItems, items, selectedRestaurants);

        // Assert
        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(0.0, order.getTotalBill());  // Bill will remain 0 since no items in the map
        assertEquals(0, order.getItems().size());
    }
}
