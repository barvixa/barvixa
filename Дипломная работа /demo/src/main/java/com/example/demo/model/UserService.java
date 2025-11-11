package com.example.demo.model;

import com.example.demo.repository.UsersRepository;
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
    
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        try {
            // Валидация входных данных
            String validationError = validateUserRequest(request);
            if (validationError != null) {
                return new CreateUserResponse(false, validationError);
            }
            
            // Проверяем, существует ли пользователь с таким email
            if (usersRepository.existsByEmail(request.getEmail())) {
                return new CreateUserResponse(false, "Пользователь с таким email уже существует");
            }
            
            // Создаем нового пользователя
            User newUser = createUserFromRequest(request);
            User savedUser = usersRepository.save(newUser);
            
            logger.info("Пользователь успешно создан с ID: {}", savedUser.getId());
            return new CreateUserResponse(true, "Пользователь успешно создан с ID: " + savedUser.getId());
            
        } catch (DataAccessException e) {
            logger.error("Ошибка доступа к данным при создании пользователя: {}", e.getMessage());
            return new CreateUserResponse(false, "Ошибка базы данных при создании пользователя");
        } catch (Exception e) {
            logger.error("Непредвиденная ошибка при создании пользователя: {}", e.getMessage(), e);
            return new CreateUserResponse(false, "Внутренняя ошибка сервера");
        }
    }
    
    private String validateUserRequest(CreateUserRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Имя пользователя обязательно";
        }
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return "Email обязателен";
        }
        
        // Базовая валидация email
        if (!isValidEmail(request.getEmail())) {
            return "Некорректный формат email";
        }
        
        return null;
    }
    
    private boolean isValidEmail(String email) {
        // Простая валидация email
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private User createUserFromRequest(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }
        
        if (request.getInitialBonus() != null) {
            user.setBonusPoints(request.getInitialBonus());
        } else {
            user.setBonusPoints(0); // значение по умолчанию
        }
        
        return user;
    }
    
    // Вложенные DTO классы
    public static class CreateUserRequest {
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
    
    public static class CreateUserResponse {
        private boolean success;
        private String message;

        // Конструкторы
        public CreateUserResponse() {}

        public CreateUserResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
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
    }
}