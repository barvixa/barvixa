package com.example.demo.service;

import com.example.demo.dto.CreateUserStatusDto;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.UsersRepository;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private UsersRepository usersRepository;

    @Transactional(noRollbackFor = {RuntimeException.class})
    public CreateUserStatusDto updateUserWithQuery(Long userId, CreateUserStatusDto request) {
        try {
            if (!usersRepository.existsById(userId)) {
                return createErrorResponse("User not found with id: " + userId, request);
            }
            
            UserDto existingUser = usersRepository.findById(userId).orElseThrow();
            
            if (request.getName() != null && 
                !existingUser.getUsername().equals(request.getName()) && 
                usersRepository.existsByUsername(request.getName())) {
                return createErrorResponse("Username is already in use: " + request.getName(), request);
            }
            
            if (request.getEmail() != null && 
                !existingUser.getEmail().equals(request.getEmail()) && 
                usersRepository.existsByEmail(request.getEmail())) {
                return createErrorResponse("Email is already in use: " + request.getEmail(), request);
            }
            
            usersRepository.updateUser(
                userId,
                request.getName() != null ? request.getName() : existingUser.getUsername(),
                request.getEmail() != null ? request.getEmail() : existingUser.getEmail(),
                request.getPhone() != null ? request.getPhone() : existingUser.getPhone(),
                request.getInitialBonus() != null ? request.getInitialBonus() : existingUser.getBonusPoints(),
                request.getBonusPoints() != null ? request.getBonusPoints() : existingUser.getBonusPoints(),
                request.getUserGroup() != null ? request.getUserGroup() : existingUser.getUserGroup()
            );
            
            return createSuccessResponse("User updated successfully", request);
            
        } catch (Exception e) {
            logger.error("Error updating user with ID {}: {}", userId, e.getMessage());
            return createErrorResponse("Error updating user: " + e.getMessage(), request);
        }
    }
    
@Transactional
public CreateUserStatusDto deleteUser(Long userId) {
    CreateUserStatusDto response = new CreateUserStatusDto();
    try {

        if (!usersRepository.existsById(userId)) {
            response.setSuccess(false);
            response.setMessage("User not found with id: " + userId);
            return response;
        }
        

        UserDto user = usersRepository.findById(userId).orElseThrow();
        

        usersRepository.deleteById(userId);
        
        logger.info("User successfully deleted with ID: {}", userId);
        
        response.setSuccess(true);
        response.setMessage("User successfully deleted");
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        
        return response;
        
    } catch (Exception e) {
        logger.error("Error deleting user with ID {}: {}", userId, e.getMessage());
        response.setSuccess(false);
        response.setMessage("Error deleting user: " + e.getMessage());
        return response;
    }
}


private CreateUserStatusDto createSuccessResponse(String message, CreateUserStatusDto request) {
    CreateUserStatusDto response = new CreateUserStatusDto();
    response.setSuccess(true);
    response.setMessage(message);
    response.setEmail(request.getEmail());
    response.setName(request.getName());
    response.setPhone(request.getPhone());
    response.setInitialBonus(request.getInitialBonus());
    return response;
}

private CreateUserStatusDto createErrorResponse(String errorMessage, CreateUserStatusDto request) {
    CreateUserStatusDto response = new CreateUserStatusDto();
    response.setSuccess(false);
    response.setMessage(errorMessage);
    response.setEmail(request.getEmail());
    response.setName(request.getName());
    response.setPhone(request.getPhone());
    response.setInitialBonus(request.getInitialBonus());
    return response;
}
    

    private String validateUserRequest(CreateUserStatusDto request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Имя пользователя обязательно";
        }
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return  "Email обязателен";
        }
        

        if (!isValidEmail(request.getEmail())) {
            return "Некорректный формат email";
        }

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            if (!isValidPhone(request.getPhone())) {
                return "Некорректный формат телефона";
            }
        }
        
        return null;
    }
    

    private boolean isValidEmail(String email) {

        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    

    private boolean isValidPhone(String phone) {

        return phone != null && phone.matches("^[+]?[0-9]{10,15}$");
    }
    

    private UserDto createUserFromRequest(CreateUserStatusDto request) {
        UserDto user = new UserDto();
        user.setUsername(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone().trim());
        }
        
        if (request.getInitialBonus() != null) {
            user.setBonusPoints(String.valueOf(request.getInitialBonus()));
        } else {
            user.setBonusPoints("0"); 
        }
        
        return user;
    }
    public List<UserDto> findAll() {
      return usersRepository.findAll();
    }
    
    @Transactional
public CreateUserStatusDto createUser(CreateUserStatusDto request) {
    CreateUserStatusDto response = new CreateUserStatusDto();
    try {
        logger.info("Начало создания пользователя с email: {}", request.getEmail());
        
        String validationError = validateUserRequest(request);
        if (validationError != null) {
            logger.warn("Ошибка валидации: {}", validationError);
            response.setSuccess(false);
            response.setMessage(validationError);
            response.setEmail(request.getEmail());
            response.setName(request.getName());
            response.setPhone(request.getPhone());
            response.setInitialBonus(request.getInitialBonus());
            return response;
        }

        if (usersRepository.existsByEmail(request.getEmail())) {
            String errorMessage = "Пользователь с email " + request.getEmail() + " уже существует";
            logger.warn(errorMessage);
            response.setSuccess(false);
            response.setMessage(errorMessage);
            response.setEmail(request.getEmail());
            response.setName(request.getName());
            response.setPhone(request.getPhone());
            response.setInitialBonus(request.getInitialBonus());
            return response;
        }

        UserDto newUser = createUserFromRequest(request);
        UserDto savedUser = usersRepository.save(newUser);
        
        logger.info("Пользователь успешно создан с ID: {}", savedUser.getId());

        response.setSuccess(true);
        response.setMessage("Пользователь успешно создан");
        response.setEmail(savedUser.getEmail());
        response.setName(savedUser.getName());
        response.setPhone(savedUser.getPhone());
        response.setInitialBonus(savedUser.getBonusPoints()); 
        
        return response;
        
    } catch (DataAccessException e) {
        logger.error("Ошибка доступа к данным при создании пользователя: {}", e.getMessage());
        response.setSuccess(false);
        response.setMessage("Ошибка базы данных: " + e.getMessage());
        response.setEmail(request.getEmail());
        response.setName(request.getName());
        response.setPhone(request.getPhone());
        response.setInitialBonus(request.getInitialBonus());
        return response;
    } catch (Exception e) {
        logger.error("Непредвиденная ошибка при создании пользователя: {}", e.getMessage(), e);
        response.setSuccess(false);
        response.setMessage("Внутренняя ошибка сервера");
        response.setEmail(request.getEmail());
        response.setName(request.getName());
        response.setPhone(request.getPhone());
        response.setInitialBonus(request.getInitialBonus());
        return response;
    }
    
}
public BigDecimal getTotalBonusBalance() {
    List<UserDto> users = usersRepository.findAll();
    return users.stream()
        .map(user -> {logger.info(user.getBonusPoints());
            String bonusStr = user.getBonusPoints();
            if (bonusStr != null && !bonusStr.trim().isEmpty()) {
                try {
 
                    String cleaned = bonusStr.trim()
                                          .replace(" ", "")
                                          .replace(",", ".");
                    return new BigDecimal(cleaned);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid bonus format for user {}: {}", user.getId(), bonusStr);
                    return BigDecimal.ZERO;
                }
            }
            return BigDecimal.ZERO;
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
}
}