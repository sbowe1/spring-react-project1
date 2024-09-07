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

	private SubtopicDao subtopicDao;

	private TopicDao topicDao;

	@Autowired
	public SubtopicService(SubtopicDao subtopicDao, TopicDao topicDao) {
		this.subtopicDao = subtopicDao;
		this.topicDao = topicDao;
	}

	/**
	 * Creates a new subtopic.
	 * @param topicId
	 * @param subtopicDto
	 * @return Subtopic
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
	 * Reads a subtopic by id.
	 * @param subtopicId
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
	 * Updates a subtopic's status.
	 * @param subtopicId
	 * @return Subtopic
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
