package com.example.p1_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.p1_backend.models.Resource;

@Repository
public interface ResourceDao extends JpaRepository<Resource, Integer> {
	/**
	 * Finds all resources by topic id.
	 * @param	topicId
	 * @return	List<Resource>
	 */
	List<Resource> findAllByTopicTopicId(int topicId);

}
