package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.services.UserService;

@RestController
@RequestMapping("users")
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

    // TODO: READ, UPDATE, DELETE
}
