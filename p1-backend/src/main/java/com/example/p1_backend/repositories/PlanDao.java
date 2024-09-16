package com.example.p1_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.Plan;

@Repository
public interface PlanDao extends JpaRepository<Plan, Integer> {

	/**
	 * Finds a plan by its name and user's ID. Multiple users may have plans with the same
	 * name, so plans are uniquely identified by plan ID, or by plan name + user ID.
	 * @param name The name of the plan to search for in the database
	 * @param userId The ID of the user
	 * @return Optional<Plan>
	 */
	Optional<Plan> getByNameAndUserUserId(String name, int userId);

}
