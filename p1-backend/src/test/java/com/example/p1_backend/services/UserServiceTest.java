package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.LoginDto;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.repositories.UserDao;
import com.example.p1_backend.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Mock
	private UserDao uDao;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private UserService us;

	@Test
	public void findAll() {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIxMTkzOTY2LCJleHAiOjE3MjEyODAzNjZ9.UEVBOz2smZknOVjQmRcKgcF1DiR8osedb2kYgV7XxGA";
		ArrayList roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		roles.add("ROLE_ADMIN");

		when(jwtUtil.extractRoles(token)).thenReturn(roles);
		Mockito.doReturn(getMockUsers(5)).when(uDao).findAll();
		List<User> users = this.us.findAll("Bearer " + token);

		assertEquals(5, users.size());
	}

	private Iterable<User> getMockUsers(int size) {
		List<User> users = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			users.add(new User("test-email" + i + "@test.com", "test-password", "test-username" + i, "ROLE_USER",
					"test-plan-" + i));
		}
		return users;
	}

	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	private String getToken() {
		// This function broke???? so here's an example token I got from postman
		return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";
	}

	// TODO: Review JUnit docs to include a descriptive string; let the tests be the documentation!
	// CREATE
	@Test
	void register() {
		// TODO: call password encode here to remove it below! -Lauren
		RegisterDto registerDto = new RegisterDto("test-user-email@test.com", "test-user-password",
				"test-user-username");
		User user = getMockUser();
		// Because Users create their own plans, there should not be a default plan
		user.setPlans(new ArrayList<>());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Set expected behavior
		// check unique username
		when(uDao.getByUsername(registerDto.getUsername())).thenReturn(Optional.empty());
		// check unique email
		when(uDao.getByEmail(registerDto.getEmail())).thenReturn(Optional.empty());

		when(uDao.save(any(User.class))).thenReturn(user);

		// Act
		User result = us.register(registerDto);

		// Assert
		assertNotNull(result);
		assertEquals("test-user-email@test.com", result.getEmail());
		assertTrue(passwordEncoder.matches("test-user-password", result.getPassword()));
		assertEquals("test-user-username", result.getUsername());
		// TODO: add another assert to ensure getRoles is not null
		assertEquals("ROLE_USER", result.getRoles().get(0));
		assertEquals(new ArrayList<>(), result.getPlans());
	}

	// READ
	@Test
	public void findByUserId() throws AccountNotFoundException {
		// Arrange
		String token = getToken();

		User mockUser = getMockUser();
		mockUser.setPassword(passwordEncoder.encode(mockUser.getPassword()));
		mockUser.setUserId(1);

		// Mock the behavior of the repository to return the mock user
		// Set expected behavior
		when(jwtUtil.extractUserId(token)).thenReturn(1);
		Mockito.when(uDao.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

		// Act
		User result = us.findByUserId("Bearer " + token);

		// Assert
		assertNotNull(result);
		assertEquals(mockUser.getUserId(), result.getUserId());
		assertEquals("test-user-email@test.com", result.getEmail());
		assertTrue(passwordEncoder.matches("test-user-password", result.getPassword()));
		assertEquals("test-user-username", result.getUsername());
		assertEquals("ROLE_USER", result.getRoles().get(0)); // TODO: grab roles
																// dynamically
		assertEquals("Spring Boot Roadmap", result.getPlans().get(0));
	}

	// TODO: Add method, findByUserIdAccountNotFoundException

	// UPDATE
	@Test
	void update() throws AccountNotFoundException {
		// Arrange
		String token = getToken();

		User currentUser = getMockUser();
		currentUser.setPassword(passwordEncoder.encode(currentUser.getPassword()));
		currentUser.setUserId(1);

		// Set expected behavior
		when(jwtUtil.extractUserId(token)).thenReturn(1);
		when(uDao.save(any(User.class))).thenReturn(currentUser);
		when(uDao.findById(anyInt())).thenReturn(Optional.of(currentUser));
		when(jwtUtil.generateToken(any(User.class))).thenReturn(token);

		// Act
		User updatedUser = new User(currentUser.getEmail(), currentUser.getPassword(), currentUser.getUsername(),
				currentUser.getRoles(), currentUser.getPlans());
		updatedUser.setUserId(currentUser.getUserId());

		String result = us.update("Bearer " + token, updatedUser);

		// Assert
		assertNotNull(result);
		assertEquals(currentUser.getUserId(), updatedUser.getUserId());
		assertEquals("test-user-email@test.com", updatedUser.getEmail());
		assertTrue(passwordEncoder.matches("test-user-password", updatedUser.getPassword()));
		assertEquals("test-user-username", updatedUser.getUsername());
		assertEquals("ROLE_USER", updatedUser.getRoles().get(0)); // TODO: grab roles
																	// dynamically
		assertEquals("Spring Boot Roadmap", updatedUser.getPlans().get(0));
		assertEquals(getToken(), result);
		verify(uDao, times(1)).findById(anyInt());
		verify(uDao, atMost(1)).getByUsername(anyString());
		verify(uDao, atMost(1)).getByEmail(anyString());
	}

	// TODO: add test for validations: email unique, etc.
	// TODO: Add method, updateAccountNotFoundException

	// DELETE
	@Test
	void delete() throws AccountNotFoundException {
		String token = getToken();

		when(jwtUtil.extractUserId(token)).thenReturn(1);
		when(uDao.findById(anyInt())).thenReturn(Optional.of(getMockUser())).thenReturn(Optional.empty());
		// TODO: investigate if below is necessary?
		//doNothing().when(uDao).deleteById(1);

		String message = us.delete("Bearer " + token);

		assertEquals("Account deleted successfully!", message);
		verify(jwtUtil, times(1)).extractUserId(token);
		verify(uDao, times(1)).deleteById(1);
		verify(uDao, atMost(2)).findById(1);
	}

	// TODO: Add method, deleteAccountNotFoundException

	// LOGIN
	@Test
	public void login() throws AccountNotFoundException {
		User mockUser = getMockUser();
		mockUser.setUserId(1);

		String token = getToken();

		LoginDto loginDto = new LoginDto(mockUser.getUsername(), mockUser.getPassword());

		mockUser.setPassword(passwordEncoder.encode(mockUser.getPassword()));

		when(uDao.getByUsername(loginDto.getUsername())).thenReturn(Optional.of(mockUser));
		when(jwtUtil.generateToken(mockUser)).thenReturn(token);

		String result = us.login(loginDto);

		assertNotNull(result);
		assertEquals(token, result);
	}

}
