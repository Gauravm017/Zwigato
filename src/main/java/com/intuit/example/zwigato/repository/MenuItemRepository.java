package com.intuit.example.zwigato.repository;

import com.intuit.example.zwigato.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    //List<MenuItem> findByName(String name);

    MenuItem findByName(String name);

    //List<MenuItem> findByName(List<String> names);
}
