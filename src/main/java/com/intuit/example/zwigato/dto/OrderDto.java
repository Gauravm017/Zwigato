package com.intuit.example.zwigato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.intuit.example.zwigato.model.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private double totalBill;
    private List<OrderItemDto> items;
}
