package com.example.demo.status;

import com.example.demo.dto.CreateUserStatusDto;

public class StatusSuccess {
   public CreateUserStatusDto setSucess(String message){
    CreateUserStatusDto response = new CreateUserStatusDto(); 

    response.setSuccess(false);
    response.setMessage(message);
    response.setEmail(request.getEmail());
    response.setName(request.getName());
    response.setPhone(request.getPhone());
    response.setInitialBonus(request.getInitialBonus());
                    
    return response;
   }
}
