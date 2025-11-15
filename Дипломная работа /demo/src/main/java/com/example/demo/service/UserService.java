package com.example.demo.service;

import com.example.demo.dto.CreateUserStatusDto;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.status.StatusError;
import com.example.demo.status.StatusSuccess;


import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private UserValidateService userValidateService;
    @Autowired
    private UserCreateFromRequestService userCreateFromRequestService;
    @Autowired
    private StatusSuccess statusSuccess;
    @Autowired
    private StatusError statusError;

    @Transactional(noRollbackFor = {RuntimeException.class})
    public CreateUserStatusDto updateUserWithQuery(Long userId, CreateUserStatusDto request) {
        try {
            if (!usersRepository.existsById(userId)) {
                return statusSuccess.setSucess("User not found with id: " + userId,false,request);
            }
            
            UserDto existingUser = usersRepository.findById(userId).orElseThrow();
            
            if (request.getName() != null && 
                !existingUser.getUsername().equals(request.getName()) && 
                usersRepository.existsByUsername(request.getName())) {
                return statusSuccess.setSucess("Username is already in use: " + request.getName(),false,request);
            }
            
            if (request.getEmail() != null && 
                !existingUser.getEmail().equals(request.getEmail()) && 
                usersRepository.existsByEmail(request.getEmail())) {
                 return statusSuccess.setSucess("Email is already in use: " + request.getEmail(),false,request);
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

            return statusSuccess.setSucess("Email is already in use: " + request.getEmail(),true,request); 
        } catch (Exception e) {
            logger.error("Error updating user with ID {}: {}", userId, e.getMessage());
           return statusError.setError("Error updating user with ID {}: {}" + userId + e.getMessage(),false,request);
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

    public List<UserDto> findAll() {
      return usersRepository.findAll();
    }
    
    @Transactional
    public CreateUserStatusDto createUser(CreateUserStatusDto request) {
        try {
            logger.info("Начало создания пользователя с email: {}", request.getEmail());
            
            String validationError = userValidateService.validateUserRequest(request);
            if (validationError != null) {
                logger.warn("Ошибка валидации: {}", validationError);
                return statusSuccess.setSucess(validationError,false,request);
            }

            if (usersRepository.existsByEmail(request.getEmail())) {
                String errorMessage = "Пользователь с email " + request.getEmail() + " уже существует";
                logger.warn(errorMessage);
                 return statusSuccess.setSucess(errorMessage,false,request);
            }
            
            UserDto newUser = userCreateFromRequestService.createUserFromRequest(request);
            UserDto savedUser = usersRepository.save(newUser);
            
            logger.info("Пользователь успешно создан с ID: {}", savedUser.getId());
             return statusSuccess.setSucess("Пользователь успешно создан",true,request);
            } 

            catch (DataAccessException e) {
            logger.error("Ошибка доступа к данным при создании пользователя: {}", e.getMessage());
             return statusSuccess.setSucess("Ошибка базы данных: " + e.getMessage(),false,request);
            } 
            catch (Exception e) {
             return statusSuccess.setSucess("Внутренняя ошибка сервера",false,request);
            }
    }

    public BigDecimal getTotalBonusBalance() {
        return findAll().stream()
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