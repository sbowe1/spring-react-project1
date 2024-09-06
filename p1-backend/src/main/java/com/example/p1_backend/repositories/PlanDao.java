package com.example.p1_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.Plan;

@Repository
public interface PlanDao extends JpaRepository<Plan, Integer> {

	/**
	 * Finds a plan by name.
	 * @param name
	 * @return Optional<Plan>
	 */
	Optional<Plan> getByName(String name);

}
