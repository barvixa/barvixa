package com.example.demo.controller;

import com.example.demo.dto.AuthDto;
import com.example.demo.repository.AuthRepository;
import com.example.demo.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@Controller
@CrossOrigin("*")
@RequestMapping("/api/user/auth")
@AllArgsConstructor
public class AuthController {
    public JdbcTemplate jdbcTemplate;
    // private AuthRepository userRepo;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login (
            @RequestBody AuthDto loginForm,
            HttpServletResponse response,
            HttpServletRequest request,
            AuthRepository userRepo
    ) {
        return LoginService.login(loginForm, userRepo, jdbcTemplate);
    }
}
