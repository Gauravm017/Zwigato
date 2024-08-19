package com.intuit.example.zwigato.service;

import com.intuit.example.zwigato.exception.DatabaseOperationException;
import com.intuit.example.zwigato.exception.ResourceNotFoundException;
import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.repository.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllMenuItems() {
        try {
            return menuItemRepository.findAll();
        }
        catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new DatabaseOperationException("Failed to update menu", e);
        }
    }
}
