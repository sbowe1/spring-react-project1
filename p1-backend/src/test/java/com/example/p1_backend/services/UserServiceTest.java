package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.p1_backend.models.dtos.LoginDto;
import com.example.p1_backend.models.dtos.OutUserDto;
import com.example.p1_backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.repositories.UserDao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.AccountNotFoundException;

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
        Mockito.doReturn(getMockUsers(5)).when(uDao).findAll();
        List<User> users = this.us.findAll();
        assertEquals(5, users.size());
    }

    private Iterable<User> getMockUsers(int size) {
        List<User> users = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            users.add(
                new User(
                    "test-email" + i + "@test.com",
                    "test-password",
                    "test-username" + i,
                    "ROLE_USER",
                    "test-plan-" + i
                )
            );
        }
        return users;
    }

    private User getMockUser() {
        return new User(
            "test-user-email@test.com",
            "test-user-password",
            "test-user-username",
            "ROLE_USER",
            "Spring Boot Roadmap"
        );
    }

    private String getToken(){
        User user = getMockUser();
        user.setUserId(1);
        return jwtUtil.generateToken(user);
    }

    // CREATE
    @Test
    void register() {
        RegisterDto registerDto = new RegisterDto("test-user-email@test.com", "test-user-password", "test-user-username");
        User user = getMockUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set expected behavior
        when(uDao.getByUsername(registerDto.getUsername())).thenReturn(Optional.empty());
        when(uDao.getByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
        when(uDao.save(any(User.class))).thenReturn(user);
        
        // Act
        User result = us.register(registerDto);
        
        // Assert
        assertNotNull(result);
        assertEquals("test-user-email@test.com", result.getEmail());
        assertTrue(passwordEncoder.matches("test-user-password", result.getPassword()));
        assertEquals("test-user-username", result.getUsername());
        assertEquals("ROLE_USER", result.getRoles().get(0));
        assertEquals("Spring Boot Roadmap", result.getPlans().get(0));
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
        OutUserDto result = us.findByUserId(token);

        // Assert
        assertNotNull(result);
        assertEquals("test-user-username", result.getUsername());
        assertEquals("ROLE_USER", result.getRoles().get(0)); // TODO: grab roles dynamically
        assertEquals("Spring Boot Roadmap", result.getPlans().get(0));
        assertEquals(token, result.getToken());
    }

    // UPDATE
    @Test
    void update() throws AccountNotFoundException {
        // Arrange
        String token = getToken();

        User mockUser = getMockUser();
        mockUser.setPassword(passwordEncoder.encode(mockUser.getPassword()));
        mockUser.setUserId(1);

        // Set expected behavior
        when(jwtUtil.extractUserId(token)).thenReturn(1);
        when(uDao.save(any(User.class))).thenReturn(mockUser);
        when(uDao.findById(anyInt())).thenReturn(Optional.of(mockUser));

        // Act
        User user = new User(
            mockUser.getEmail(),
            passwordEncoder.encode(mockUser.getPassword()),
            mockUser.getUsername(),
            mockUser.getRoles().get(0),
            mockUser.getPlans().get(0)
        );
        user.setUserId(mockUser.getUserId());

        user = us.update(token, user);

        // Assert
        assertNotNull(user);
        assertEquals(mockUser.getUserId(), user.getUserId());
        assertEquals("test-user-email@test.com", user.getEmail());
        assertTrue(passwordEncoder.matches("test-user-password", user.getPassword()));
        assertEquals("test-user-username", user.getUsername());
        assertEquals("ROLE_USER", user.getRoles().get(0)); // TODO: grab roles dynamically
        assertEquals("Spring Boot Roadmap", user.getPlans().get(0));
        verify(uDao, times(1)).findById(anyInt());
    }

    // DELETE
    @Test
    void delete() {
        String token = getToken();

        when(jwtUtil.extractUserId(token)).thenReturn(1);
        doNothing().when(uDao).deleteById(1);

        String message = us.delete(token);

        assertEquals("Account deleted successfully!", message);
        verify(jwtUtil, times(1)).extractUserId(token);
        verify(uDao, times(1)).deleteById(1);
        verify(uDao, times(1)).findById(1);
    }

    // LOGIN
    @Test
    public void login() throws AccountNotFoundException {
        String token = getToken();
        User mockUser = getMockUser();
        mockUser.setUserId(1);
        LoginDto loginDto = new LoginDto(
          mockUser.getUsername(),
          mockUser.getPassword()
        );
        mockUser.setPassword(passwordEncoder.encode(mockUser.getPassword()));

        when(uDao.getByUsername(loginDto.getUsername())).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(mockUser)).thenReturn(token);

        String result = us.login(loginDto);

        assertNotNull(result);
        assertEquals(token, result.getToken());
    }
}
