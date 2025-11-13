package com.example.demo.service;

import com.example.demo.dto.CreateUserResponse;
import com.example.demo.dto.User;
import com.example.demo.repository.UsersRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UsersRepository usersRepository;
    
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    
    /**
     * Метод для создания нового пользователя
     */
@Transactional(noRollbackFor = {RuntimeException.class})
public void updateUserWithQuery(Long userId, CreateUserResponse request) {
    try {
        // Проверяем существование пользователя
        if (!usersRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        
        // Получаем текущего пользователя для проверок
        User existingUser = usersRepository.findById(userId).orElseThrow();
        
        // Проверяем уникальность username
        if (request.getName() != null && 
            !existingUser.getUsername().equals(request.getName()) && 
            usersRepository.existsByUsername(request.getName())) {
            throw new RuntimeException("Username is already in use: " + request.getName());
        }
        
        // Проверяем уникальность email
        if (request.getEmail() != null && 
            !existingUser.getEmail().equals(request.getEmail()) && 
            usersRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use: " + request.getEmail());
        }
        
        // Вызываем метод репозитория
        usersRepository.updateUser(
            userId,
            request.getName() != null ? request.getName() : existingUser.getUsername(),
            request.getEmail() != null ? request.getEmail() : existingUser.getEmail(),
            request.getPhone() != null ? request.getPhone() : existingUser.getPhone(),
            request.getBonusBalance() != null ? request.getBonusBalance() : existingUser.getBonusBalance(),
            request.getBonusPoints() != null ? request.getBonusPoints() : existingUser.getBonusPoints(),
            request.getUserGroup() != null ? request.getUserGroup() : existingUser.getUserGroup()
        );
        
    } catch (Exception e) {
        logger.error("Error updating user with ID {}: {}", userId, e.getMessage());
        throw e; // Пробрасываем исключение дальше
    }
}
    
    /**
     * Валидация данных запроса
     */
    private String validateUserRequest(CreateUserResponse request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Имя пользователя обязательно";
        }
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return  "Email обязателен";
        }
        
        // Базовая валидация email
        if (!isValidEmail(request.getEmail())) {
            return "Некорректный формат email";
        }
        
        // Валидация телефона (если указан)
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            if (!isValidPhone(request.getPhone())) {
                return "Некорректный формат телефона";
            }
        }
        
        return null;
    }
    
    /**
     * Проверка формата email
     */
    private boolean isValidEmail(String email) {
        // Простая валидация email
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Проверка формата телефона
     */
    private boolean isValidPhone(String phone) {
        // Простая валидация телефона (только цифры, может начинаться с +)
        return phone != null && phone.matches("^[+]?[0-9]{10,15}$");
    }
    
    /**
     * Создание объекта User из запроса
     */
    private User createUserFromRequest(CreateUserResponse request) {
        User user = new User();
        user.setUsername(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone().trim());
        }
        
        if (request.getInitialBonus() != null) {
            user.setBonusPoints(String.valueOf(request.getInitialBonus()));
        } else {
            user.setBonusPoints("0"); // значение по умолчанию
        }
        
        return user;
    }
    public List<User> findAll() {
      return usersRepository.findAll();
    }
    
    @Transactional
public CreateUserResponse createUser(CreateUserResponse request) {
   CreateUserResponse response = new CreateUserResponse();
    try {
        logger.info("Начало создания пользователя с email: {}", request.getEmail());
        
        // Валидация входных данных
        String validationError = validateUserRequest(request);
        if (validationError != null) {
            logger.warn("Ошибка валидации: {}", validationError);
        response.setSuccess(false);
        response.setEmail(request.getEmail());
        response.setName(request.getName());
        return response;
        }
        
        // Проверяем, существует ли пользователь с таким email
        if (usersRepository.existsByEmail(request.getEmail())) {
            String errorMessage = "Пользователь с email " + request.getEmail() + " уже существует";
            logger.warn(errorMessage);
        response.setSuccess(false);
        response.setEmail(request.getEmail());
        response.setName(request.getName());
        return response;
        }
        
        // Создаем нового пользователя
        User newUser = createUserFromRequest(request);
        User savedUser = usersRepository.save(newUser);
        
        logger.info("Пользователь успешно создан с ID: {}", savedUser.getId());
        
        // Возвращаем успешный ответ
 response.setSuccess(true);
        response.setEmail(request.getEmail());
        response.setName(request.getName());
        return response;
        
    } catch (DataAccessException e) {
        logger.error("Ошибка доступа к данным при создании пользователя: {}", e.getMessage());
      response.setSuccess(false);
        response.setEmail(request.getEmail());
        response.setName(request.getName());
        return response;
    } catch (Exception e) {
        logger.error("Непредвиденная ошибка при создании пользователя: {}", e.getMessage(), e);
     response.setSuccess(false);
        response.setEmail(request.getEmail());
        response.setName(request.getName());
        return response;
    }
}
}