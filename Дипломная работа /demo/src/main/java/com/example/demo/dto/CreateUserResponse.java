package com.example.demo.dto;

public class CreateUserResponse {
    private boolean success;
    private String message;
    private String email;
    private String name;
    private String phone;
    private String initialBonus;
    private String bonusBalance;
    private String bonusPoints;
    private String userGroup;
    // Конструкторы
    public CreateUserResponse() {}

    public CreateUserResponse(boolean success, String message, String email, String name, String phone, String initialBonus,String bonusBalance,String bonusPoints, String userGroup ) {
        this.success = success;
        this.message = message;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.initialBonus = initialBonus;
        this.bonusBalance = bonusBalance;
        this.bonusPoints = bonusPoints;
        this.userGroup = userGroup;
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
    public void setPhone(String phone) {
        this.phone = phone;
    }
     public String getPhone() {
        return phone;
    }
     public void setInitialBonus(String initialBonus) {
        this.initialBonus = initialBonus;
    }
     public String getInitialBonus() {
        return initialBonus;
    }
    public void setBonusBalance(String bonusBalance) {
        this.bonusBalance = bonusBalance;
    }
     public String getBonusBalance() {
        return bonusBalance;
    }
    public void setBonusPoints(String bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
     public String getBonusPoints() {
        return bonusPoints;
    }
    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }
     public String getUserGroup() {
        return userGroup;
    }
}