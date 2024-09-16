package com.example.p1_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.Subtopic;

@Repository
public interface SubtopicDao extends JpaRepository<Subtopic, Integer> {

	/**
	 * Finds all subtopics by its topic ID.
	 * @param topicId The ID of the topic associated with the subtopic(s)
	 * @return List<Subtopic>
	 */
	List<Subtopic> findAllByTopicTopicId(int topicId);

}
