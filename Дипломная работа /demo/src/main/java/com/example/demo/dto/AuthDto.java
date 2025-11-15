package com.example.demo.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthDto {
    @JsonProperty("login")
    private String login;
    @NotNull
    @JsonProperty("password")
    private String password;
}
