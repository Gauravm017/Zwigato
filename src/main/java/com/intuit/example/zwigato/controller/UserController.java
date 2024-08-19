package com.intuit.example.zwigato.controller;

import com.intuit.example.zwigato.dto.UserDto;
import com.intuit.example.zwigato.mapper.MapperService;
import com.intuit.example.zwigato.model.User;
import com.intuit.example.zwigato.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        UserDto userDto = MapperService.convertUserToDto(userService.createUser(user));
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto userDto = MapperService.convertUserToDto(userService.getUserById(userId));
         return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
