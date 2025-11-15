package com.example.demo.repository;  

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.UserDto;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Long> {
    
    // Метод для проверки существования пользователя по email
    boolean existsByEmail(String email);
    
    // Метод для проверки существования пользователя по имени
    boolean existsByUsername(String username);
    
    // Метод для поиска пользователя по email
    UserDto findByEmail(String email);
    
    // Метод для поиска по ID (опционально, для лучшей обработки)
    Optional<UserDto> findById(Long id);
    
    // Метод для обновления пользователя с учетом ваших полей
    @Modifying
    @Query("UPDATE UserDto u SET u.username = :username, u.email = :email, u.phone = :phone, u.bonusBalance = :bonusBalance, u.bonusPoints = :bonusPoints, u.userGroup = :userGroup WHERE u.id = :id")
    void updateUser(
        @Param("id") Long id,
        @Param("username") String username,
        @Param("email") String email,
        @Param("phone") String phone,
        @Param("bonusBalance") String bonusBalance,
        @Param("bonusPoints") String bonusPoints,
        @Param("userGroup") String userGroup
    );
    
}
