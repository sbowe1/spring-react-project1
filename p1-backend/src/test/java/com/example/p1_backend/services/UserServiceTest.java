package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.RegisterDto;
import com.example.p1_backend.repositories.UserDao;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao uDao;

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

    // CREATE
    @Test
    void register() {
        RegisterDto registerDto = new RegisterDto("test-user-email@test.com", "test-user-password", "test-user-username");
        User user = getMockUser();
        
        // Set expected behavior
        when(uDao.getByUsername(registerDto.getUsername())).thenReturn(Optional.empty());
        when(uDao.getByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
        when(uDao.save(any(User.class))).thenReturn(user);
        
        // Act
        User result = us.register(registerDto);
        
        // Assert
        assertNotNull(result);
        assertEquals("test-user-email@test.com", result.getEmail());
        assertEquals("test-user-password", result.getPassword());
        assertEquals("test-user-username", result.getUsername());
        assertEquals("ROLE_USER", result.getRoles().get(0));
        assertEquals("Spring Boot Roadmap", result.getPlans().get(0));
    }

    // READ
    @Test
    public void findByUserId() {
        // Arrange
        User mockUser = getMockUser();

        mockUser.setUserId(1);

        // Mock the behavior of the repository to return the mock user
        // Set expected behavior
        Mockito.when(uDao.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

        // Act
        User result = us.findByUserId(mockUser.getUserId());

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.getUserId(), result.getUserId());
        assertEquals("test-user-email@test.com", result.getEmail());
        assertEquals("test-user-password", result.getPassword());
        assertEquals("test-user-username", result.getUsername());
        assertEquals("ROLE_USER", result.getRoles().get(0)); // TODO: grab roles dynamically
        assertEquals("Spring Boot Roadmap", result.getPlans().get(0));
    }

    // UPDATE
    @Test
    void update() {
        // Arrange
        User mockUser = getMockUser();
        mockUser.setUserId(1);

        // Set expected behavior
        when(uDao.save(any(User.class))).thenReturn(mockUser);

        // Act
        User user = new User(
            mockUser.getEmail(),
            mockUser.getPassword(),
            mockUser.getUsername(),
            mockUser.getRoles().get(0),
            mockUser.getPlans().get(0)
        );

        user = us.update(user);

        // Assert
        assertNotNull(user);
        assertEquals(mockUser.getUserId(), user.getUserId());
        assertEquals("test-user-email@test.com", user.getEmail());
        assertEquals("test-user-password", user.getPassword());
        assertEquals("test-user-username", user.getUsername());
        assertEquals("ROLE_USER", user.getRoles().get(0)); // TODO: grab roles dynamically
        assertEquals("Spring Boot Roadmap", user.getPlans().get(0));
    }

    // DELETE
    @Test
    void delete() {
        doNothing().when(uDao).deleteById(1); // TODO: use mockito's verify(?) instead of doNothing
        us.delete(1);
    }

}