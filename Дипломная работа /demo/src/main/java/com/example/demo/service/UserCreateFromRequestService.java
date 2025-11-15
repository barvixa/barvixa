package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.CreateUserStatusDto;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.AuthRepository;
@Service
public class UserCreateFromRequestService {

    @Autowired
    private AuthRepository userRepository;
   public UserDto createUserFromRequest(CreateUserStatusDto request) {
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
        
        user.setPassword(userRepository.encodePassword(request.getPassword()));

        return user;
    } 
}
