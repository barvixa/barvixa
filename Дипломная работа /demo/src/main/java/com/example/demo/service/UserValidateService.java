package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateUserStatusDto;

@Service
public class UserValidateService {
    private boolean isValidEmail(String email) {

        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidPhone(String phone) {

        return phone != null && phone.matches("^[+]?[0-9]{10,15}$");
    }

     public String validateUserRequest(CreateUserStatusDto request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Имя пользователя обязательно";
        }
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return  "Email обязателен";
        }
        
        if (!isValidEmail(request.getEmail())) {
            return "Некорректный формат email";
        }

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            if (!isValidPhone(request.getPhone())) {
                return "Некорректный формат телефона";
            }
        }
        
        return null;
    }
}
