package com.example.demo.dto;

public class CreateUserResponse {
    private boolean success;
    private String message;
    private String email;
    private String name;
    // Конструкторы
    public CreateUserResponse() {}

    public CreateUserResponse(boolean success, String message, String emale, String email, String name) {
        this.success = success;
        this.message = message;
        this.email = email;
        this.name = name;
    }

    // Геттеры и сеттеры
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmail(String email) {
        this.email = email;
    }
     public String getEmail() {
        return email;
    }
     public void setName(String name) {
        this.name = name;
    }
     public String getName() {
        return name;
    }
}