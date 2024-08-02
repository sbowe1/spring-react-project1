package com.example.p1_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.p1_backend.models.dtos.LoginDto;
import com.example.p1_backend.repositories.PlanDao;
import com.example.p1_backend.util.JwtUtil;
import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

	public List<User> findAll(String token) {
		ArrayList<String> roles = jwtUtil.extractRoles(token.substring(7));

		if (!roles.contains("ROLE_ADMIN")) {
			log.warn("Permission denied");
			throw new AccessDeniedException("You do not have permission to access");
		}

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

		// validate email is a valid email address and unique
		String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		if (registerDto.getEmail().isBlank() || !registerDto.getEmail().matches(emailRegex)) {
			log.warn("Account must be linked to an email address");
			throw new IllegalArgumentException("Account must be linked to a valid email address");
		}
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

		// persist user to database
		log.info("User {} created successfully", newUser.getUsername());
		return uDao.save(newUser); // TODO: return webDto? (no password, id, etc.)
	}

	// READ
	public User findByUserId(String token) throws AccountNotFoundException {
		// Substring to remove "Bearer " from the token String
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = uDao.findById(userId);
		if (optUser.isEmpty()) {
			log.warn("User not found");
			throw new AccountNotFoundException("User with userId: " + userId + " not found");
		}
		return uDao.findById(userId).get();
	}

	// UPDATE
	public String update(String token, User updatedUser) throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = uDao.findById(userId);
		if (optUser.isEmpty()) {
			throw new AccountNotFoundException("User with userId: " + userId + " not found");
		}

		if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
			// Ensuring that Username is not taken
			Optional<User> optUser2 = uDao.getByUsername(updatedUser.getUsername());
			if (optUser2.isPresent() && optUser2.get().getUserId() != optUser.get().getUserId()) {
				log.warn("Username is already taken");
				throw new IllegalArgumentException("Username is already taken!");
			}

			optUser.get().setUsername(updatedUser.getUsername());
		}
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			optUser.get().setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}
		if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
			// Ensuring that Email is not taken
			Optional<User> optUser3 = uDao.getByEmail(updatedUser.getEmail());
			if (optUser3.isPresent() && !optUser3.get().getEmail().equals(optUser.get().getEmail())) {
				log.warn("Email is already linked to another account");
				throw new IllegalArgumentException("Email is already linked to an account!");
			}

			optUser.get().setEmail(updatedUser.getEmail());
		}
		if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
			optUser.get().setRoles(updatedUser.getRoles());
		}
		if (updatedUser.getPlans() != null && !updatedUser.getPlans().isEmpty()) {
			optUser.get().setPlans(updatedUser.getPlans());
		}

		log.info("User with userId: {}'s information updated", optUser.get().getUserId());
		uDao.save(optUser.get());
		return jwtUtil.generateToken(optUser.get());
	}

	// DELETE
	@Transactional
	public String delete(String token) throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = uDao.findById(userId);
		if (optUser.isEmpty()) {
			log.warn("User does not exist");
			throw new AccountNotFoundException("User does not exist");
		}

		uDao.deleteById(userId);

		if (uDao.findById(userId).isPresent()) {
			log.warn("Cannot delete account!");
			return "Could not delete account";
		}

		log.info("User with userId: {}'s account deleted", userId);
		return "Account deleted successfully!";
	}

	public String login(LoginDto loginDto) throws AccountNotFoundException {
		Optional<User> optUser = uDao.getByUsername(loginDto.getUsername());

		if (optUser.isEmpty()) {
			log.warn("Account not found");
			throw new AccountNotFoundException("User with username: " + loginDto.getUsername() + " not found");
		}

		if (passwordEncoder.matches(loginDto.getPassword(), optUser.get().getPassword())) {
			String token = jwtUtil.generateToken(optUser.get());
			log.info("Login successful");
			return token;
		}
		else {
			log.warn("Incorrect login credentials");
			return null;
		}
	}

}
