package com.example.p1_backend.repositories;

import com.example.p1_backend.models.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicDao extends JpaRepository<Subtopic, Integer> {

	List<Subtopic> findAllByTopicTopicId(int topicId);

}
