package com.intuit.example.zwigato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDto {
    private Long id;
    private String name;
    private int maxCapacity;
    private int currentProcessingLoad;
    private double rating;
    private List<RestaurantItemDto> restaurantItemDtoList;

}
