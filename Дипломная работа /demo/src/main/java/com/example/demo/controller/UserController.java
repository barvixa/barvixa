package com.example.demo.controller;


import com.example.demo.dto.CreateUserStatusDto;
import com.example.demo.dto.UserDto;
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
    public CreateUserStatusDto createUser(@RequestBody CreateUserStatusDto request) {
        return userService.createUser(request);
        
    }

    @GetMapping("/all")
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @PutMapping("/update/{id}")
    public CreateUserStatusDto updateUser(
        @PathVariable Long id, 
        @RequestBody CreateUserStatusDto request
    ) {
        return userService.updateUserWithQuery(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public CreateUserStatusDto deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/bonuses/total")
    public ResponseEntity<BigDecimal> getTotalBonuses() {
        BigDecimal total = userService.getTotalBonusBalance();
        return ResponseEntity.ok(total);
    }
}