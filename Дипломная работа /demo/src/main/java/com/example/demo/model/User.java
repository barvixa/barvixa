package com.example.demo.model;  // Добавьте эту строку!

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String phone;
    
    @Column(name = "bonus_points")
    private int bonusPoints = 0;
 
    // Конструкторы
    public User() {}
    
    public User(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
    
    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public int getBonusPoints() { return bonusPoints; }
    public void setBonusPoints(int bonusPoints) { this.bonusPoints = bonusPoints; }
}