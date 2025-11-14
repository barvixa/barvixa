package com.example.demo.status;

import com.example.demo.dto.CreateUserStatusDto;

public class StatusError {
    public CreateUserStatusDto setError(String message,Boolean status, CreateUserStatusDto request ){
    CreateUserStatusDto response = new CreateUserStatusDto(); 
    
    response.setSuccess(status);
    response.setMessage(message);
    response.setEmail(request.getEmail());
    response.setName(request.getName());
    response.setPhone(request.getPhone());
    response.setInitialBonus(request.getInitialBonus());
                    
    return response;
   }
}
