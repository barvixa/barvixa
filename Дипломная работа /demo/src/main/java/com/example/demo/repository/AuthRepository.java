package com.example.demo.repository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.AuthDto;
import com.example.demo.repository.AuthRepository;

@Service
@AllArgsConstructor
@Repository
@Controller
@RequestMapping
@CrossOrigin("*")
public class AuthRepository {
     public String encodePassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(rawPassword.getBytes());
            return Base64.getEncoder().encodeToString(encodedHash);
        } 
        
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хэширования", e);
        }
    }

    public ResponseEntity<?> create(
        AuthDto auth,
        JdbcTemplate jdbcTemplate
    ) {
        try {
            jdbcTemplate.update(
                "insert into public.users(username, password_hash) values (?, ?)",
                auth.getLogin(),
                encodePassword(auth.getPassword())
            );

            return new ResponseEntity<>("", HttpStatus.OK);
        }
        
        catch (DataAccessException exception) {
            return new ResponseEntity<>("Ошибка: "+exception, HttpStatus.BAD_REQUEST);
        }
    }

    public boolean validPassword (
        @NotNull String username,
        @NotNull String password,
        JdbcTemplate jdbcTemplate
    ) {
        try {
            var hashed = jdbcTemplate.queryForObject (
                "select password from users where username=?",
                String.class, username
            );

            return hashed.equals(encodePassword(password));
        }

        catch (DataAccessException exception) {
            return false;
        }
    }

    public String getUserId(
        @NotNull String username,
        JdbcTemplate jdbcTemplate
    ) {
        try {
            var hashed = jdbcTemplate.queryForObject(
                "select id from users where username=?",
                String.class, 
                username
            );

            return hashed;
        }

        catch (DataAccessException exception) {
            return "0";
        }        
    }
}
