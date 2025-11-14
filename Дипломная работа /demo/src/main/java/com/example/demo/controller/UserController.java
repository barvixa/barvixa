package com.example.demo.controller;


import com.example.demo.dto.CreateUserResponse;
import com.example.demo.dto.User;
import com.example.demo.service.UserService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public CreateUserResponse createUser(@RequestBody CreateUserResponse request) {
        return userService.createUser(request);
        
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PutMapping("/update/{id}")
    public CreateUserResponse updateUser(
        @PathVariable Long id, 
        @RequestBody CreateUserResponse request
    ) {
        return userService.updateUserWithQuery(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public CreateUserResponse deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/bonuses/total")
    public ResponseEntity<BigDecimal> getTotalBonuses() {
        BigDecimal total = userService.getTotalBonusBalance();
        return ResponseEntity.ok(total);
    }
}