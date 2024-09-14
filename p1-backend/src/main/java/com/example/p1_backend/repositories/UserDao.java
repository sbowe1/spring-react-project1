package com.example.p1_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

	/**
	 * Finds a user by its name.
	 * @param name
	 * @return Optional<User>
	 */
	Optional<User> getByName(String name);

	/**
	 * Finds a user by its email.
	 * @param email
	 * @return Optional<User>
	 */
	Optional<User> getByEmail(String email);

}