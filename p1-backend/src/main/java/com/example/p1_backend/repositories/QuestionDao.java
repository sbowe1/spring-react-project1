package com.example.p1_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.Question;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

	/**
	 * Finds all questions by user ID.
	 * @param userId The ID of the user associated with the question(s)
	 * @return List<Question>
	 */
	List<Question> findAllByUserUserId(int userId);

	/**
	 * Finds all questions by topic ID.
	 * @param topicId The ID of the topic associated with the question(s)
	 * @return List<Question>
	 */
	List<Question> findAllByTopicTopicId(int topicId);

}
