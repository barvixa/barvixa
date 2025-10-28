package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.UsersRepository;
import com.example.demo.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Operations with users")
public class TestController {
    
    private final UsersRepository usersRepository;
    
    public TestController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    
    // Корневой endpoint для /api/users
    @GetMapping("/")
    @Operation(summary = "Get home page", description = "Returns home page message")
    public String home() {
        return "Дипломный проект: Система бонусов и лояльности работает!";
    }
    
    @GetMapping("/test")
    public String test() {
        return "Тестовый endpoint работает! H2 Console: http://localhost:8080/h2-console";
    }
    
    @GetMapping("/health")
    public String health() {
        return "Приложение здорово! Время: " + java.time.LocalDateTime.now();
    }
    
    // СОЗДАНИЕ пользователя
    @PostMapping("/create")
    @Operation(summary = "Create new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {
        try {
            // Валидация входных данных
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return new CreateUserResponse(false, "Имя пользователя обязательно");
            }
            
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return new CreateUserResponse(false, "Email обязателен");
            }
            
            // Проверяем, существует ли пользователь с таким email
            if (usersRepository.existsByEmail(request.getEmail())) {
                return new CreateUserResponse(false, "Пользователь с таким email уже существует");
            }
            
            // Создаем нового пользователя
            User newUser = new User();
            newUser.setUsername(request.getName());
            newUser.setEmail(request.getEmail());
            newUser.setPhone(request.getPhone());
            newUser.setBonusPoints(request.getInitialBonus());
            // newUser.setRegistrationDate(java.time.LocalDateTime.now());
            
            User savedUser = usersRepository.save(newUser);
            
            return new CreateUserResponse(true, "Пользователь успешно создан с ID: " + savedUser.getId());
            
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return new CreateUserResponse(false, "Ошибка при создании пользователя: " + e.getMessage());
        }
    }
    
    // ПОЛУЧЕНИЕ всех пользователей
    @GetMapping("/list")
    @Operation(summary = "Get all users", description = "Returns list of all users")
    public List<UserResponse> getAllUsers() {
        List<User> users = usersRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getBonusPoints()
                    // user.getRegistrationDate()
, null
            ))
                .collect(Collectors.toList());
    }
    
    // ПОЛУЧЕНИЕ пользователя по ID
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns user by specified ID")
    public UserResponse getUserById(@PathVariable Long id) {
        Optional<User> user = usersRepository.findById(id);
        if (user.isPresent()) {
            User foundUser = user.get();
            return new UserResponse(
                foundUser.getId(),
                foundUser.getUsername(),
                foundUser.getEmail(),
                foundUser.getPhone(),
                foundUser.getBonusPoints()
                // foundUser.getRegistrationDate()
, null
            );
        } else {
            throw new RuntimeException("Пользователь с ID " + id + " не найден");
        }
    }
    
    // ОБНОВЛЕНИЕ пользователя
    @PutMapping("/update/{id}")
    @Operation(summary = "Update user", description = "Updates user information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public UpdateUserResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        try {
            Optional<User> userOptional = usersRepository.findById(id);
            if (userOptional.isEmpty()) {
                return new UpdateUserResponse(false, "Пользователь с ID " + id + " не найден");
            }
            
            User user = userOptional.get();
            
            // Валидация данных
            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                user.setUsername(request.getName());
            }
            
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                // Проверяем, не занят ли email другим пользователем
                User existingUser = usersRepository.findByEmail(request.getEmail());
                if (existingUser != null && !existingUser.getId().equals(id)) {
                    return new UpdateUserResponse(false, "Email уже используется другим пользователем");
                }
                user.setEmail(request.getEmail());
            }
            
            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
            }
            
            if (request.getBonusPoints() >= 0) {
                user.setBonusPoints(request.getBonusPoints());
            }
            
            usersRepository.save(user);
            return new UpdateUserResponse(true, "Пользователь успешно обновлен");
            
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return new UpdateUserResponse(false, "Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }
    
    // УДАЛЕНИЕ пользователя
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user", description = "Deletes user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public DeleteUserResponse deleteUser(@PathVariable Long id) {
        try {
            if (!usersRepository.existsById(id)) {
                return new DeleteUserResponse(false, "Пользователь с ID " + id + " не найден");
            }
            
            usersRepository.deleteById(id);
            return new DeleteUserResponse(true, "Пользователь успешно удален");
            
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return new DeleteUserResponse(false, "Ошибка при удалении пользователя: " + e.getMessage());
        }
    }
    
    // DTO классы
    
    // Для создания пользователя
    public static class CreateUserRequest {
        @Schema(description = "Имя пользователя", example = "Иван Иванов")
        private String name;
        
        @Schema(description = "Email пользователя", example = "ivan@example.com")
        private String email;
        
        @Schema(description = "Телефон пользователя", example = "+79161234567")
        private String phone;
        
        @Schema(description = "Начальное количество бонусных баллов", example = "100")
        private int initialBonus = 0;
        
        // Геттеры и сеттеры
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public int getInitialBonus() { return initialBonus; }
        public void setInitialBonus(int initialBonus) { this.initialBonus = initialBonus; }
    }
    
    // Для обновления пользователя
    public static class UpdateUserRequest {
        @Schema(description = "Имя пользователя", example = "Иван Иванов")
        private String name;
        
        @Schema(description = "Email пользователя", example = "ivan@example.com")
        private String email;
        
        @Schema(description = "Телефон пользователя", example = "+79161234567")
        private String phone;
        
        @Schema(description = "Количество бонусных баллов", example = "150")
        private int bonusPoints = -1; // -1 означает, что поле не обновляется
        
        // Геттеры и сеттеры
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public int getBonusPoints() { return bonusPoints; }
        public void setBonusPoints(int bonusPoints) { this.bonusPoints = bonusPoints; }
    }
    
    // Ответы API
    public static class CreateUserResponse {
        private boolean success;
        private String message;
        
        public CreateUserResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    public static class UpdateUserResponse {
        private boolean success;
        private String message;
        
        public UpdateUserResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    public static class DeleteUserResponse {
        private boolean success;
        private String message;
        
        public DeleteUserResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    // Для ответа с данными пользователя
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private int bonusPoints;
        // private java.time.LocalDateTime registrationDate;
        
        public UserResponse(Long id, String name, String email, String phone, int bonusPoints, java.time.LocalDateTime registrationDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.bonusPoints = bonusPoints;
            // this.registrationDate = registrationDate;
        }
        
        // Геттеры
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public int getBonusPoints() { return bonusPoints; }
        // public java.time.LocalDateTime getRegistrationDate() { return registrationDate; }
    }
}