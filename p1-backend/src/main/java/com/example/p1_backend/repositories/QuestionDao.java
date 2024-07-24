package com.example.p1_backend.repositories;

import com.example.p1_backend.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

	List<Question> findAllByUserUserId(int userId);

	List<Question> findAllByTopicTopicId(int topicId);

}
