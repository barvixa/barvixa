package com.example.demo.repository;  

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    
    // Метод для проверки существования пользователя по email
    boolean existsByEmail(String email);
    
    // Метод для проверки существования пользователя по имени
    boolean existsByUsername(String username);
    
    // Метод для поиска пользователя по email
    User findByEmail(String email);
}