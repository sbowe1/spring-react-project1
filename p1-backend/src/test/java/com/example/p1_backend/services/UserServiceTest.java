package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.p1_backend.models.User;
import com.example.p1_backend.repositories.UserDao;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService us;

    @Mock
    private UserDao uDao;

    private User getMockAdmin() {
        return new User(
            "test-admin-email@test.com",
            "test-admin-password",
            "test-admin-username",
            "ROLE_ADMIN"
        );
    }

    @Test
    void getAll() {
        Mockito.doReturn(getMockUsers(2)).when(uDao).findAll();
        List<User> users = this.us.getAll();
        assertEquals(2, users.size());
    }

    private Iterable<User> getMockUsers(int size) {
        List<User> users = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            users.add(
                new User(
                    "test-email" + i + "@test.com",
                    "test-password",
                    "test-username" + i,
                    "ROLE_USER"
                )
            );
        }
        return users;
    }
}

// User
    // userId (int)
    // email (String)
    // password (String) *hashed
    // username (String)
    // roles (String[])
    // plans (int[])