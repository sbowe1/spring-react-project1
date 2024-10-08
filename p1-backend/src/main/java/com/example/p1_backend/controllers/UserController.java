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
	 * Registers a new user. First checks that email is a valid email address and unique.
	 * Finally, checks that password meets requirements.
	 * @param registerDto DTO consisting of: email, password, name
	 * @return Successful registration message
	 */
	@PostMapping("register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
		String result = us.register(registerDto);
		return new ResponseEntity<>(result, CREATED);
	}

	/**
	 * Views the account details of the user.
	 * @param token The user's Authorization token
	 * @return User object, excluding its password
	 * @throws AccountNotFoundException If the user is not found
	 */
	@GetMapping("account-details")
	public ResponseEntity<User> accountDetails(@RequestHeader("Authorization") String token)
			throws AccountNotFoundException {
		User user = us.findByUserId(token);
		return new ResponseEntity<>(user, OK);
	}

	/**
	 * Views all users in the database.
	 * @param token The user's Authorization token
	 * @return List of all users in the database if the user has the role "ROLE_ADMIN"
	 */
	@GetMapping
	public ResponseEntity<List<User>> viewAllUsers(@RequestHeader("Authorization") String token) {
		List<User> users = us.findAll(token);
		return new ResponseEntity<>(users, OK);
	}

	/**
	 * Updates the user's account details.
	 * @param token The user's Authorization token
	 * @param updateUser A user object consisting of all fields to be updated
	 * @return The user's updated Authorization token
	 * @throws AccountNotFoundException If the user is not found
	 */
	@PutMapping("update")
	public ResponseEntity<String> update(@RequestHeader("Authorization") String token, @RequestBody User updateUser)
			throws AccountNotFoundException {
		String message = us.update(token, updateUser);
		return new ResponseEntity<>(message, OK);
	}

	/**
	 * Deletes the user's account.
	 * @param token The user's Authorization
	 * @return Successful deletion message
	 * @throws AccountNotFoundException If the user is not found
	 */
	@DeleteMapping
	public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token)
			throws AccountNotFoundException {
		String message = us.delete(token);
		return new ResponseEntity<>(message, OK);
	}

	/**
	 * Logs in the user.
	 * @param loginDto DTO consisting of: email, password
	 * @return The user's Authorization token; Failure message if password does not match
	 * @throws AccountNotFoundException If the user is not found
	 */
	@PostMapping("login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) throws AccountNotFoundException {
		String token = us.login(loginDto);
		return new ResponseEntity<>(token, OK);
	}

}
