package com.example.demo.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserDto {
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
}