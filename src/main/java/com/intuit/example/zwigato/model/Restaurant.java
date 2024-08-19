package com.intuit.example.zwigato.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Restaurant")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @Column(name = "max_capacity", updatable = false, nullable = false)
    private int maxCapacity;

    @Getter
    @Setter
    @Column(name = "currentProcessingLoad")
    private int currentProcessingLoad;

    @Getter
    @Setter
    @Column(name = "rating")
    private double rating;

    @Getter
    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RestaurantMenuItem> restaurantMenuItem;

    public Restaurant() {}

    public Restaurant(String name, int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.name = name;
    }
}

