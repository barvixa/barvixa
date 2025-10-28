package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.UsersRepository;
import com.example.demo.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Operations with users")
public class UserController {
    
    public final UsersRepository usersRepository;
    
    public UserController(UsersRepository usersRepository) {
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
    
    @PostMapping("/create")
    @Operation(summary = "Create new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
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
            
            User savedUser = usersRepository.save(newUser);
            
            return new CreateUserResponse(true, "Пользователь успешно создан с ID: " + savedUser.getId());
            
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return new CreateUserResponse(false, "Ошибка при создании пользователя: " + e.getMessage());
        }
    }
    
    // DTO класс для запроса создания пользователя
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
    @GetMapping("/all")
@Operation(summary = "Get all users", description = "Returns list of all users")
public List<User> getUsers() {
    return usersRepository.findAll();
}
    
    // DTO класс для ответа
    public static class CreateUserResponse {
        private boolean success;
        private String message;
        
        public CreateUserResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        // Геттеры
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
