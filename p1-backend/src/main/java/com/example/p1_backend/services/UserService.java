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
	 * @return List of Users in the database
	 */
	public List<User> findAll(String token) {
		ArrayList<String> roles = jwtUtil.extractRoles(token.substring(7));

		if (!roles.contains("ROLE_ADMIN")) {
			log.warn("Permission denied");
			throw new AccessDeniedException("Permission denied");
		}

		return uDao.findAll();
	}

	/**
	 * Registers a new user. First checks that the email is a valid email address and
	 * unique. Finally, checks that the password meets the requirements. Password
	 * Requirements: - 1 Upper Case Letter - 1 Special Character - No White Space - 8-64
	 * Characters
	 * @param registerDto DTO consisting of: email, password, name
	 * @return Successful creation String
	 */
	public String register(RegisterDto registerDto) {
		// validate email is a valid email address and unique
		String emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		if (registerDto.getEmail().isBlank() || !registerDto.getEmail().matches(emailRegex)) {
			log.warn("Account must be linked to an email address");
			throw new IllegalArgumentException("Enter in the format: name@example.com");
		}
		Optional<User> email = uDao.getByEmail(registerDto.getEmail());
		if (email.isPresent()) {
			log.warn("Email already in use");
			throw new IllegalArgumentException("Email: " + registerDto.getEmail() + " is unavailable");
		}

		// Password must contain:
		// at least 1 upper case letter (?=.*[A-Z])
		// at least one special character (?=.*[!@#$%^&+=])
		// no white space (?=\S+$)
		// length 8-64 char .{8,64}
		String passwordRegex = "^(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,64}$";
		if (registerDto.getPassword().isBlank() || !(registerDto.getPassword()).matches(passwordRegex)) {
			log.warn("Password does not meet requirements");
			throw new IllegalArgumentException(
					"Password must: contain 1 upper case letter, contain 1 special character, "
							+ ", have no white space, and be at least 8 characters long");
		}

		// create new user if name and email are unique
		User newUser = new User();
		newUser.setEmail(registerDto.getEmail());
		newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		newUser.setName(registerDto.getUsername());
		newUser.getRoles().add("ROLE_USER");

		// persist user to database
		uDao.save(newUser);

		log.info("User {} created successfully", newUser.getName());
		return "User " + newUser.getName() + " created successfully";
	}

	/**
	 * Finds a user by its userId. Only the user with the userId in the token is allowed
	 * to access this method.
	 * @param token Authorization token
	 * @return User object
	 */
	public User findByUserId(String token) throws AccountNotFoundException {
		// Substring to remove "Bearer " from the token String
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = uDao.findById(userId);
		if (optUser.isEmpty()) {
			log.warn("User not found");
			throw new AccountNotFoundException("Account does not exist");
		}
		return optUser.get();
	}

	/**
	 * Updates a user's account information. Only the user with the userId in the token is
	 * allowed to access this method.
	 * @param token Authorization token
	 * @param updatedUser Full/Partial User object with fields to be updated
	 * @return User's updated Authorization token
	 * @throws AccountNotFoundException If the User object is not found
	 */
	public String update(String token, User updatedUser) throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = uDao.findById(userId);
		if (optUser.isEmpty()) {
			log.warn("User not found");
			throw new AccountNotFoundException("Account does not exist");
		}

		if (updatedUser.getName() != null && !updatedUser.getName().isBlank()) {
			// Ensuring that name is not taken
			Optional<User> optUser2 = uDao.getByName(updatedUser.getName());
			if (optUser2.isPresent() && optUser2.get().getUserId() != userId) {
				log.warn("name is already taken");
				throw new IllegalArgumentException("Update failed; Invalid name");
			}

			optUser.get().setName(updatedUser.getName());
		}
		String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,16}$";
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			if (!updatedUser.getPassword().matches(passwordRegex)) {
				log.warn("Password does not meet requirements");
				throw new IllegalArgumentException(
						"Password must: contain 1 upper case letter, contain 1 special character, "
								+ ", have no white space, and be at least 8 characters long");
			}
			optUser.get().setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}
		if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
			// Ensuring that Email is not taken
			Optional<User> optUser3 = uDao.getByEmail(updatedUser.getEmail());
			if (optUser3.isPresent() && optUser3.get().getUserId() != userId) {
				log.warn("Email is already linked to another account");
				throw new IllegalArgumentException("Email is unavailable");
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
	 * @param token Authorization token
	 * @return Successful deletion String
	 * @throws AccountNotFoundException If the User object is not found
	 */
	@Transactional
	public String delete(String token) throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = uDao.findById(userId);
		if (optUser.isEmpty()) {
			log.warn("User does not exist");
			throw new AccountNotFoundException("Account does not exist");
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
	 * Logs in a user. If the user is found in the database and the password matches, an
	 * authorization token is generated.
	 * @param loginDto DTO consisting of: name, password
	 * @return User's Authorization token
	 * @throws AccountNotFoundException If the User object is not found
	 */
	public String login(LoginDto loginDto) throws AccountNotFoundException {
		Optional<User> optUser = uDao.getByEmail(loginDto.getEmail());

		if (optUser.isEmpty()) {
			log.warn("Account not found");
			throw new AccountNotFoundException("Login failed! Invalid email or password");
		}

		if (passwordEncoder.matches(loginDto.getPassword(), optUser.get().getPassword())) {
			String token = jwtUtil.generateToken(optUser.get());
			log.info("Login successful");
			return token;
		}
		else {
			log.warn("Login failed; Invalid name or password");
			return null;
		}
	}

}
