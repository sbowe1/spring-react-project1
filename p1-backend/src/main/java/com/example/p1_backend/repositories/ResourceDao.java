package com.example.p1_backend.repositories;

import com.example.p1_backend.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceDao extends JpaRepository<Resource, Integer> {

	List<Resource> findAllByTopicTopicId(int topicId);

}
