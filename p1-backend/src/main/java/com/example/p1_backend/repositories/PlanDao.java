package com.example.p1_backend.repositories;

import com.example.p1_backend.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanDao extends JpaRepository<Plan, Integer> {

	Optional<Plan> getByName(String name);

}
