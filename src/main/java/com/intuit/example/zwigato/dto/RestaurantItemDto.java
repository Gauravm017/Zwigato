package com.intuit.example.zwigato.dto;

import lombok.Data;

@Data
public class RestaurantItemDto {
    private Long id;
    private String name;
    private double price;
}
