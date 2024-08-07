package com.example.p1_backend.services;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.dtos.InTopicDto;
import com.example.p1_backend.repositories.PlanDao;
import com.example.p1_backend.repositories.TopicDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class TopicService {

	private TopicDao topicDao;

	private PlanDao planDao;

	@Autowired
	public TopicService(TopicDao topicDao, PlanDao planDao) {
		this.topicDao = topicDao;
		this.planDao = planDao;
	}

	// CREATE
	public Topic createTopic(int planId, InTopicDto topicDto) {
		Optional<Plan> optPlan = planDao.findById(planId);
		if (optPlan.isEmpty()) {
			log.warn("Plan does not exist");
			throw new NoSuchElementException("Plan does not exist");
		}

		log.info("Topic: {} created successfully", topicDto.getTopicName());
		return topicDao.save(new Topic(topicDto.getTopicName(), topicDto.getDescription(), optPlan.get(), false));
	}

	// READ
	public Topic readTopic(int topicId) {
		Optional<Topic> optTopic = topicDao.findById(topicId);
		if (optTopic.isEmpty()) {
			log.warn("Topic does not exist");
			throw new NoSuchElementException("Topic does not exist");
		}

		return optTopic.get();
	}

	// UPDATE
	public Topic updateTopic(int topicId) {
		Optional<Topic> optTopic = topicDao.findById(topicId);
		if (optTopic.isEmpty()) {
			log.warn("Topic does not exist");
			throw new NoSuchElementException("Topic does not exist");
		}

		optTopic.get().setStatus(!optTopic.get().isStatus());
		log.info("Topic: {} status updated", optTopic.get().getTitle());
		return topicDao.save(optTopic.get());
	}

}
