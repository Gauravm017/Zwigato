package com.intuit.example.zwigato.controller;

import com.intuit.example.zwigato.dto.OrderDto;
import com.intuit.example.zwigato.mapper.MapperService;
import com.intuit.example.zwigato.model.Order;
import com.intuit.example.zwigato.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@RestController
//@RequestMapping("/orders")
//public class OrderController {
//    @Autowired
//    private OrderService orderService;
//
//    @PostMapping
//    public Order placeOrder(@RequestParam String itemName, @RequestParam int quantity) {
//        return orderService.placeOrder(itemName, quantity);
//    }
//}
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<OrderDto> placeOrder(@PathVariable Long userId, @RequestBody Map<Long, Integer> items) {
        Order order = orderService.placeOrder(userId, items);
        OrderDto orderDto = MapperService.convertOrderToDto(order);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

}
