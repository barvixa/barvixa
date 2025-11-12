package com.example.demo.controller;

import com.example.demo.dto.CreateUserResponse;
import com.example.demo.dto.User;
import com.example.demo.service.UserService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public CreateUserResponse createUser(@RequestBody CreateUserResponse request) {
        return createUser(request);
        
    }
       @GetMapping("/all")
    public List<User> getUsers() {
        return userService.findAll();
    }
}