package com.example.demo.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String phone;
    
    private String bonusPoints;
    
    private String bonusBalance;

    private String userGroup;

    private String name;
    // Конструкторы
    public User() {}
    
    public User(String username, String email, String phone, String bonusPoints, String bonusBalance, String userGroup, String name) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.bonusPoints = bonusPoints;
        this.bonusBalance = bonusBalance;
        this.userGroup = userGroup;
        this.name = name;
    }
    
    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getBonusPoints() { return bonusPoints; }
    public void setBonusPoints(String bonusPoints) { this.bonusPoints = bonusPoints; }

    public  String getBonusBalance() { return bonusBalance; }
    public void setBonusBalance(String bonusBalance) { this.bonusBalance = bonusBalance; }

    public  String getUserGroup() { return userGroup; }
    public void setUserGroup(String userGroup) { this.userGroup = userGroup; }

     public  String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
}