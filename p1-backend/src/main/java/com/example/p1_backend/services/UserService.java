package com.example.p1_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.p1_backend.models.User;
import com.example.p1_backend.repositories.UserDao;

@Service
public class UserService {
    private final UserDao uDao;

    public UserService(UserDao uDao) {
        this.uDao = uDao;
    }

    public User findByUserId(int userId) {
        return uDao.findByUserId(userId).get();
    }

    public List<User> findAll() {
        return uDao.findAll();
    }

}