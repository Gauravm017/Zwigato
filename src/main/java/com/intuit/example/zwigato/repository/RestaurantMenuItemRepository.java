package com.intuit.example.zwigato.repository;

import com.intuit.example.zwigato.model.RestaurantMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantMenuItemRepository extends JpaRepository<RestaurantMenuItem,Long> {
}
