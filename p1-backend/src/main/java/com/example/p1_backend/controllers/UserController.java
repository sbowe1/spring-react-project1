package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.LoginDto;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.services.UserService;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = { "http://localhost:3000" }, allowCredentials = "true")
public class UserController {

	private final UserService us;

	@Autowired
	public UserController(UserService us) {
		this.us = us;
	}

	/**
	 * Registers a new user. First checks that username is unique. Then checks that email
	 * is a valid email address and unique. Finally, checks that password meets
	 * requirements.
	 * @param registerDto email, password, username
	 * @return String
	 */
	@PostMapping("register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
		String result = us.register(registerDto);
		return new ResponseEntity<>(result, CREATED);
	}

	/**
	 * Views the profile of the user.
	 * @param token
	 * @return User
	 * @throws AccountNotFoundException
	 */
	@GetMapping("profile")
	public ResponseEntity<User> viewProfile(@RequestHeader("Authorization") String token)
			throws AccountNotFoundException {
		User user = us.findByUserId(token);
		return new ResponseEntity<>(user, OK);
	}

	/**
	 * Views all users in the database.
	 * @param token
	 * @return String
	 */
	@GetMapping
	public ResponseEntity<List<User>> viewAllUsers(@RequestHeader("Authorization") String token) {
		List<User> users = us.findAll(token);
		return new ResponseEntity<>(users, OK);
	}

	/**
	 * Updates the user's profile.
	 * @param token
	 * @param updateUser
	 * @return String
	 * @throws AccountNotFoundException
	 */
	@PutMapping("update")
	public ResponseEntity<String> update(@RequestHeader("Authorization") String token, @RequestBody User updateUser)
			throws AccountNotFoundException {
		String message = us.update(token, updateUser);
		return new ResponseEntity<>(message, OK);
	}

	/**
	 * Deletes the user's account.
	 * @param token
	 * @return String
	 * @throws AccountNotFoundException
	 */
	@DeleteMapping
	public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token)
			throws AccountNotFoundException {
		String message = us.delete(token);
		return new ResponseEntity<>(message, OK);
	}

	/**
	 * Logs in the user.
	 * @param loginDto email, password
	 * @return String
	 * @throws AccountNotFoundException
	 */
	@PostMapping("login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) throws AccountNotFoundException {
		String token = us.login(loginDto);
		return new ResponseEntity<>(token, OK);
	}

}
