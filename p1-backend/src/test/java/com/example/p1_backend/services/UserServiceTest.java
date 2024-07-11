package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.p1_backend.models.User;
import com.example.p1_backend.repositories.UserDao;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao uDao;

    @InjectMocks
    private UserService us;

    @Test
    public void findByUserId() {
        // Arrange
        User mockUser = getMockUser();
        mockUser.setUserId(1);

        // Mock the behavior of the repository to return the mock user
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
        assertEquals("Spring Boot Roadmap", result.getPlans().get(0)); // TODO: update to int? 
    }

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
}