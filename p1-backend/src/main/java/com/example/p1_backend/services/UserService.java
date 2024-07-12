package com.example.p1_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.repositories.UserDao;

@Service
public class UserService {
    private final UserDao uDao;

    @Autowired
    public UserService(UserDao uDao) {
        this.uDao = uDao;
    }

    public List<User> findAll() {
        return uDao.findAll();
    }

    // CREATE
    public User register(RegisterDto registerDto) {
        // validate username is unique
        Optional<User> username = uDao.getByUsername(registerDto.getUsername());
        if (username.isPresent()) {
            // TODO: throw new UsernameAlreadyTakenException("Username: " + optUser.get().getUsername() + " was already taken");
        }

        // validate email is unique
        Optional<User> email = uDao.getByEmail(registerDto.getEmail());
        if (email.isPresent()) {
            // TODO: throw new EmailAlreadyTakenException("Email: " + optUser.get().getEmail() + " was already taken");
        }

        // create new user if username and email are unique
        User newUser = new User();
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(registerDto.getPassword()); // TODO: hash password
        newUser.setUsername(registerDto.getUsername());
        newUser.getRoles().add("ROLE_USER");
        newUser.getPlans().add("Spring Boot Roadmap");

        // persist user to database
        return uDao.save(newUser); // TODO: return webDto? (no password, id, etc.)
    }

    // READ
    public User findByUserId(int userId) {
        Optional<User> optUser = uDao.findById(userId);
        if (optUser.isEmpty()) {
            // TODO: throw new UserNotFoundException("User with userId: " + optUser.get().getUserId() + " not found");
        }
        return uDao.findById(userId).get(); // TODO: return webDto? (no password, id, etc.)
    }
    
    // UPDATE
    public User update(User updatedUser) {
        Optional<User> optUser = uDao.findById(updatedUser.getUserId());
        if (optUser.isEmpty()) {
            // TODO: throw new UserNotFoundException("User with userId: " + optUser.get().getUserId() + " not found");
        }
        return uDao.save(updatedUser);
    }
    
    // DELETE
    public void delete(int userId) {
        uDao.deleteById(userId);
    }

}