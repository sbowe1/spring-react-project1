package com.example.p1_backend.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String username;

    private List<String> roles = new ArrayList<>();

    private List<String> plans = new ArrayList<>();

    public User(String email, String password, String username, String role, String plan) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles.add(role);
        this.plans.add(plan);
    }
}