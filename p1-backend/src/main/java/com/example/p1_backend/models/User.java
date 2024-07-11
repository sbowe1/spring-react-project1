package com.example.p1_backend.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Component
@Data
@NoArgsConstructor
@Getter
@Setter
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

    public User(String email, String password, String username, List<String> roles, List<String> plans) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
        this.plans = plans;
    }
}