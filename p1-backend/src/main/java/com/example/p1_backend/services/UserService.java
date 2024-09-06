package com.example.p1_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.LoginDto;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.repositories.UserDao;
import com.example.p1_backend.util.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

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

	/**
	 * Finds all users in the database. Only users with the role "ROLE_ADMIN" are allowed
	 * to access this method.
	 * @param token Authorization token
	 * @return List<User>
	 */
	public List<User> findAll(String token) {
		ArrayList<String> roles = jwtUtil.extractRoles(token.substring(7));

		if (!roles.contains("ROLE_ADMIN")) {
			log.warn("Permission denied");
			throw new AccessDeniedException("You do not have permission to access");
		}

		return uDao.findAll();
	}

	/**
	 * Registers a new user. First checks that username is unique. Then checks that email
	 * is a valid email address and unique. Finally, checks that password meets
	 * requirements.
	 * @param registerDto email, password, username
	 * @return String
	 */
	public String register(RegisterDto registerDto) {
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

		// Password must contain:
		// at least 1 digit (?=.*[0-9])
		// at least 1 upper case letter (?=.*[A-Z])
		// at least one special character (?=.*[!@#$%^&+=])
		// no white space (?=\S+$)
		// length 8-16 char .{8,16}
		String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,16}$";
		if (registerDto.getPassword().isBlank() || !(registerDto.getPassword()).matches(passwordRegex)) {
			log.warn("Password does not meet requirements");
			throw new IllegalArgumentException("Password does not meet requirements");
		}

		// create new user if username and email are unique
		User newUser = new User();
		newUser.setEmail(registerDto.getEmail());
		newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		newUser.setUsername(registerDto.getUsername());
		newUser.getRoles().add("ROLE_USER");

		// persist user to database
		uDao.save(newUser);

		log.info("User {} created successfully", newUser.getUsername());
		return "User " + newUser.getUsername() + " created successfully";
	}

	/**
	 * Finds a user by userId. Only the user with the role "ROLE_ADMIN" or the user with
	 * the userId in the token is allowed to access this method.
	 * @param token Authorization token
	 * @return User object
	 */
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

	/**
	 * Updates a user's information. Only the user with the userId in the token is allowed
	 * to access this method.
	 * @param token
	 * @param updatedUser
	 * @return String
	 * @throws AccountNotFoundException
	 */
	public String update(String token, User updatedUser) throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = uDao.findById(userId);
		if (optUser.isEmpty()) {
			throw new AccountNotFoundException("User with userId: " + userId + " not found");
		}

		if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
			// Ensuring that Username is not taken
			Optional<User> optUser2 = uDao.getByUsername(updatedUser.getUsername());
			if (optUser2.isPresent() && optUser2.get().getUserId() != userId) {
				log.warn("Username is already taken");
				throw new IllegalArgumentException("Username is already taken!");
			}

			optUser.get().setUsername(updatedUser.getUsername());
		}
		String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,16}$";
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			if (!updatedUser.getPassword().matches(passwordRegex)) {
				log.warn("Password does not meet requirements");
				throw new IllegalArgumentException("Password does not meet the requirements");
			}
			optUser.get().setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}
		if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
			// Ensuring that Email is not taken
			Optional<User> optUser3 = uDao.getByEmail(updatedUser.getEmail());
			if (optUser3.isPresent() && optUser3.get().getUserId() != userId) {
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

	/**
	 * Deletes a user's account. Only the user with the userId in the token is allowed to
	 * access this method.
	 * @param token
	 * @return String
	 * @throws AccountNotFoundException
	 */
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

	/**
	 * Logs in a user. If the user is found and the password matches, a token is
	 * generated.
	 * @param loginDto
	 * @return String
	 * @throws AccountNotFoundException
	 */
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
