package com.example.p1_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.Topic;

@Repository
public interface TopicDao extends JpaRepository<Topic, Integer> {

	/**
	 * Finds all topics by plan ID.
	 * @param planId The ID of the plan associated with the topic(s)
	 * @return List<Topic>
	 */
	List<Topic> findAllByPlanPlanId(int planId);

}
