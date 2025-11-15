package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateUserStatusDto {
    private boolean success;
    private String message;
    private String email;
    private String name;
    private String phone;
    private String initialBonus;
    private String bonusBalance;
    private String bonusPoints;
    private String userGroup;
    private String password;
}