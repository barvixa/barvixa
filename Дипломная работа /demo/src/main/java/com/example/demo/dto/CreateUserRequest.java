package com.example.demo.dto;

public class CreateUserRequest {
    private String name;
    private String email;
    private String phone;
    private String initialBonus;
    private String bonusBalance;
    private String points;
    private String userGroup;
    private String bonusPoint;
    
 

    // Конструкторы
    public CreateUserRequest() {}

    public CreateUserRequest(String name, String email, String phone, String initialBonus, String bonusBalance,String points, String userGroup, String bonusePoint) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.initialBonus = initialBonus;
        this.bonusBalance = bonusBalance;
        this.points =points;
        this.userGroup = userGroup;
        this.bonusPoint =bonusePoint;
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

    public String getInitialBonus() {
        return initialBonus;
    }

    public void setInitialBonus(String initialBonus) {
        this.initialBonus = initialBonus;
    }
    public String getBonusBalance() {
        return bonusBalance;
    }
    public String getBonusPoints(){
        return points;
    }
    public String getUserGroup(){
        return userGroup;
    }
    public String bonusePoint(){
        return bonusPoint;
    }
}
