package com.intuit.example.zwigato.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Data
@Table(name = "restaurant_menu_item")
public class RestaurantMenuItem implements Serializable {

    //@EmbeddedId
    //private RestaurantMenuItemId id = new RestaurantMenuItemId();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    //@MapsId("menuItemId")
    @JoinColumn(name = "menuItem_id")
    private MenuItem menuItem;

    @ManyToOne
    //@MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "price")
    private double price;
}
