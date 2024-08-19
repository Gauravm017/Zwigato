package com.intuit.example.zwigato.controller;

import com.intuit.example.zwigato.dto.MenuDto;
import com.intuit.example.zwigato.mapper.MapperService;
import com.intuit.example.zwigato.model.MenuItem;
import com.intuit.example.zwigato.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/fetch")
    public ResponseEntity<List<MenuDto>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        List<MenuDto> menuDtoList = menuItems.stream().map(MapperService::convertMenuToDto).toList();
        return new ResponseEntity<>(menuDtoList, HttpStatus.OK);
    }
}

