package com.intuit.example.zwigato.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@Data
public class RestaurantMenuItemId implements Serializable {

    @Column(name = "menu_item_id")
    private Long menuItemId;

    @Column(name = "restaurant_id")
    private Long restaurantId;
}
