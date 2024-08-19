package com.intuit.example.zwigato.service;

import com.intuit.example.zwigato.exception.ResourceNotFoundException;
import com.intuit.example.zwigato.model.User;
import com.intuit.example.zwigato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

