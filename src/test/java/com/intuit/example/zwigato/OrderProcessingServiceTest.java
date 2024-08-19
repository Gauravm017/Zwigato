package com.intuit.example.zwigato;

import com.intuit.example.zwigato.model.Order;
import com.intuit.example.zwigato.model.OrderStatus;
import com.intuit.example.zwigato.repository.OrderRepository;
import com.intuit.example.zwigato.service.OrderProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderProcessingServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderProcessingService orderProcessingService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    void testPrepareOrder() throws InterruptedException {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(orderRepository).save(any(Order.class));

        orderProcessingService.prepareOrder(order);
        latch.await();

        verify(orderRepository, times(1)).save(order);
        assertEquals(OrderStatus.PREPARE, order.getStatus());
    }

    @Test
    void testDispatchOrder() throws InterruptedException {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(orderRepository).save(any(Order.class));

        orderProcessingService.dispatchOrder(order);
        latch.await();

        verify(orderRepository, times(1)).save(order);
        assertEquals(OrderStatus.DISPATCHED, order.getStatus());
    }

    @Test
    void testCompleteOrder() throws InterruptedException {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(orderRepository).save(any(Order.class));

        orderProcessingService.completeOrder(order);
        latch.await();

        verify(orderRepository, times(1)).save(order);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }
}
