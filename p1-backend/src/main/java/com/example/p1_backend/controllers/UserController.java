package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.example.p1_backend.models.dtos.LoginDto;
import com.example.p1_backend.models.dtos.OutUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.services.UserService;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class UserController {
    private final UserService us;

    public UserController(UserService us) {
        this.us = us;
    }

    // CREATE
    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody RegisterDto registerDto) {
        User newUser = us.register(registerDto);
        return new ResponseEntity<>(newUser, CREATED); // TODO: return webDto in UserService?
    }

    // READ
    @GetMapping("read")
    public ResponseEntity<OutUserDto> viewProfile(@RequestHeader("Authorization") String token) throws AccountNotFoundException {
        OutUserDto user = us.findByUserId(token);
        return new ResponseEntity<>(user, OK);
    }

    // UPDATE
    @PutMapping("update")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, User updateUser) throws AccountNotFoundException {
        User updatedUser = us.update(token, updateUser);
        return new ResponseEntity<>(updatedUser, OK);
    }

    // DELETE
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token){
        String message = us.delete(token);
        return new ResponseEntity<>(message, OK);
    }

    // LOGIN
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) throws AccountNotFoundException {
        String token = us.login(loginDto);
        return new ResponseEntity<>(token, OK);
    }
}
