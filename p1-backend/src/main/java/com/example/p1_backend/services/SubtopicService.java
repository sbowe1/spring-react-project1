package com.example.p1_backend.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.dtos.InSubtopicDto;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubtopicService {

	private final SubtopicDao subtopicDao;

	private final TopicDao topicDao;

	@Autowired
	public SubtopicService(SubtopicDao subtopicDao, TopicDao topicDao) {
		this.subtopicDao = subtopicDao;
		this.topicDao = topicDao;
	}

	/**
	 * Creates a new subtopic.
	 * @param topicId The ID of the topic associated with the new subtopic
	 * @param subtopicDto DTO consisting of: subtopic title, description
	 * @return Newly created subtopic
	 */
	public Subtopic create(int topicId, InSubtopicDto subtopicDto) {
		Optional<Topic> optTopic = topicDao.findById(topicId);
		if (optTopic.isEmpty()) {
			log.warn("Topic does not exist");
			throw new NoSuchElementException("Topic does not exist");
		}

		Subtopic subtopic = new Subtopic(subtopicDto.getTitle(), subtopicDto.getDescription(), optTopic.get(), false);

		log.info("Subtopic: {} successfully created", subtopicDto.getTitle());
		return subtopicDao.save(subtopic);
	}

	/**
	 * Reads a subtopic by its ID.
	 * @param subtopicId the ID of the subtopic to search for in the database
	 * @return Subtopic
	 */
	public Subtopic readSubtopic(int subtopicId) {
		Optional<Subtopic> optSubtopic = subtopicDao.findById(subtopicId);
		if (optSubtopic.isEmpty()) {
			log.warn("Subtopic does not exist");
			throw new NoSuchElementException("Subtopic does not exist");
		}

		return optSubtopic.get();
	}

	/**
	 * Toggles a subtopic's completion status.
	 * @param subtopicId The ID of the subtopic to be updated
	 * @return The updated subtopic
	 */
	public Subtopic updateSubtopic(int subtopicId) {
		Optional<Subtopic> optSubtopic = subtopicDao.findById(subtopicId);
		if (optSubtopic.isEmpty()) {
			log.warn("Subtopic does not exist");
			throw new NoSuchElementException("Subtopic does not exist");
		}

		optSubtopic.get().setStatus(!optSubtopic.get().isStatus());
		log.info("Subtopic: {} status updated", optSubtopic.get().getTitle());
		return subtopicDao.save(optSubtopic.get());
	}

}
