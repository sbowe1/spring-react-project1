package com.example.p1_backend.repositories;

import com.example.p1_backend.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicDao extends JpaRepository<Topic, Integer> {

	Optional<Topic> getByName(String topicName);

}
