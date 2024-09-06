package com.example.p1_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.Question;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {
	/**
	 * Finds all questions by user id.
	 * @param	userId
	 * @return	List<Question>
	 */
	List<Question> findAllByUserUserId(int userId);

	/**
	 * Finds all questions by topic id.
	 * @param	topicId
	 * @return	List<Question>
	 */
	List<Question> findAllByTopicTopicId(int topicId);

}
