package com.example.p1_backend.services;

import java.util.List;
import java.util.Optional;

import com.example.p1_backend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.repositories.UserDao;

import javax.security.auth.login.AccountNotFoundException;

@Service
@Slf4j
public class UserService {
    private final UserDao uDao;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserDao uDao, JwtUtil jwtUtil) {
        this.uDao = uDao;
        this.jwtUtil = jwtUtil;
    }

    public List<User> findAll() {
        return uDao.findAll();
    }

    // CREATE
    public User register(RegisterDto registerDto) {
        // validate username is unique
        Optional<User> username = uDao.getByUsername(registerDto.getUsername());
        if (username.isPresent()) {
            log.warn("Username already taken");
            throw new IllegalArgumentException("Username: " + registerDto.getUsername() + " was already taken");
        }

        // validate email is unique
        Optional<User> email = uDao.getByEmail(registerDto.getEmail());
        if (email.isPresent()) {
            log.warn("Email already in use");
            throw new IllegalArgumentException("Email: " + registerDto.getEmail() + " was already taken");
        }

        // create new user if username and email are unique
        User newUser = new User();
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setUsername(registerDto.getUsername());
        newUser.getRoles().add("ROLE_USER");
        newUser.getPlans().add("Spring Boot Roadmap");

        // persist user to database
        log.info("User {} created successfully", newUser.getUsername());
        return uDao.save(newUser); // TODO: return webDto? (no password, id, etc.)
    }

    // READ
    public User findByUserId(int userId) throws AccountNotFoundException {
        Optional<User> optUser = uDao.findById(userId);
        if (optUser.isEmpty()) {
            log.warn("User not found");
             throw new AccountNotFoundException("User with userId: " + userId + " not found");
        }
        return uDao.findById(userId).get(); // TODO: return webDto? (no password, id, etc.)
    }
    
    // UPDATE
    public User update(User updatedUser) throws AccountNotFoundException {
        Optional<User> optUser = uDao.findById(updatedUser.getUserId());
        if (optUser.isEmpty()) {
            throw new AccountNotFoundException("User with userId: " + updatedUser.getUserId() + " not found");
        }

        log.info("User {}'s information updated", optUser.get().getUsername());
        return uDao.save(updatedUser);
    }
    
    // DELETE
    public String delete(String token) {
        int userId = jwtUtil.extractUserId(token);

        uDao.deleteById(userId);

        if(uDao.findById(userId).isPresent()){
            log.warn("Cannot delete account!");
            return "Could not delete account";
        }

        log.info("User with userId: {}'s account deleted", userId);
        return "Account deleted successfully!";
    }

}