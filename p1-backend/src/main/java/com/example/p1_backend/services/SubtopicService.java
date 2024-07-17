package com.example.p1_backend.services;

import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.dtos.InSubtopicDto;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

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

	// CREATE
	public Subtopic createSubtopic(InSubtopicDto subtopicDto) {
		Optional<Topic> optTopic = topicDao.getByName(subtopicDto.getTopicName());

		Subtopic subtopic = new Subtopic(subtopicDto.getTitle(), subtopicDto.getDescription(), optTopic.get(), false);

		log.info("Subtopic: {} successfully created", subtopicDto.getTitle());
		return subtopicDao.save(subtopic);
	}

	// READ
	public Subtopic readSubtopic(int subtopicId) {
		Optional<Subtopic> optSubtopic = subtopicDao.findById(subtopicId);
		if (optSubtopic.isEmpty()) {
			log.warn("Subtopic does not exist");
			throw new NoSuchElementException("Subtopic does not exist");
		}

		return optSubtopic.get();
	}

	// UPDATE
	public Subtopic updateSubtopic(int subtopicId) {
		Optional<Subtopic> optSubtopic = subtopicDao.findById(subtopicId);
		if (optSubtopic.isEmpty()) {
			log.warn("Subtopic does not exist");
			throw new NoSuchElementException("Subtopic does not exist");
		}

		optSubtopic.get().setStatus(true);
		log.info("Subtopic: {} status updated to complete", optSubtopic.get().getTitle());
		return subtopicDao.save(optSubtopic.get());
	}

}
