package com.example.demo.model.dto;

public class CreateUserRequest {
    private String name;
    private String email;
    private String phone;
    private Integer initialBonus;

    // Конструкторы
    public CreateUserRequest() {}

    public CreateUserRequest(String name, String email, String phone, Integer initialBonus) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.initialBonus = initialBonus;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getInitialBonus() {
        return initialBonus;
    }

    public void setInitialBonus(Integer initialBonus) {
        this.initialBonus = initialBonus;
    }
}
