package com.example.p1_backend.services;

import com.example.p1_backend.models.*;
import com.example.p1_backend.models.dtos.PlanContent;
import com.example.p1_backend.models.dtos.SubtopicWResources;
import com.example.p1_backend.models.dtos.TopicWResources;
import com.example.p1_backend.repositories.*;
import com.example.p1_backend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class PlanService {

	private final JwtUtil jwtUtil;

	private final UserDao userDao;

	private final PlanDao planDao;

	private final TopicDao topicDao;

	private final SubtopicDao subtopicDao;

	private final ResourceDao resourceDao;

	@Autowired
	public PlanService(JwtUtil jwtUtil, UserDao userDao, PlanDao planDao, TopicDao topicDao, SubtopicDao subtopicDao,
			ResourceDao resourceDao) {
		this.jwtUtil = jwtUtil;
		this.userDao = userDao;
		this.planDao = planDao;
		this.topicDao = topicDao;
		this.subtopicDao = subtopicDao;
		this.resourceDao = resourceDao;
	}

	// CREATE
	public Plan createPlan(String token, String name) throws AccountNotFoundException {
		Optional<Plan> optPlan = planDao.getByName(name);
		if (optPlan.isPresent()) {
			log.warn("That plan already exists");
			throw new KeyAlreadyExistsException("That plan already exists");
		}

		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = userDao.findById(userId);
		if (optUser.isEmpty()) {
			throw new AccountNotFoundException("User with userId: " + userId + " not found");
		}

		log.info("Plan: {} created successfully", name);
		Plan plan = planDao.save(new Plan(name, optUser.get()));

		// After creating plan, add it to User's profile
		optUser.get().getPlans().add(plan.getName());
		optUser.get().setPlans(optUser.get().getPlans());
		userDao.save(optUser.get());

		return plan;
	}

	// READ
	public Plan readPlan(int planId) {
		Optional<Plan> optPlan = planDao.findById(planId);
		if (optPlan.isEmpty()) {
			log.warn("Plan does not exist");
			throw new NoSuchElementException("Plan does not exist");
		}

		return optPlan.get();
	}

	// READ CONTENTS
	public PlanContent readPlanContents(String token, int planId) {
		int userId = jwtUtil.extractUserId(token.substring(7));
		Optional<Plan> optPlan = planDao.findById(planId);
		if (optPlan.isEmpty() || optPlan.get().getUser().getUserId() != userId) {
			log.warn("Plan does not exist");
			throw new NoSuchElementException("Plan does not exist");
		}

		PlanContent planContent = new PlanContent();
		planContent.setPlanId(optPlan.get().getPlanId());
		planContent.setPlanName(optPlan.get().getName());

		List<Object> content = new ArrayList<>();
		List<Topic> topicList = topicDao.findAllByPlanPlanId(planId);
		for (Topic topic : topicList) {
			List<Resource> allResources = resourceDao.findAllByTopicTopicId(topic.getTopicId());
			// Topic with its Resources
			TopicWResources topicWResources = new TopicWResources(topic.getTopicId(), topic.getTitle(),
					topic.isStatus());

			List<Resource> topicResources = new ArrayList<>();
			for (Resource r : allResources) {
				if (r.getSubtopic() == null) {
					topicResources.add(r);
				}
			}
			topicWResources.setResources(topicResources);
			content.add(topicWResources);

			// All Subtopics under specific Topic
			List<Subtopic> subtopicList = subtopicDao.findAllByTopicTopicId(topic.getTopicId());
			for (Subtopic subtopic : subtopicList) {
				SubtopicWResources subtopicWResources = new SubtopicWResources(subtopic.getSubtopicId(),
						subtopic.getTitle(), subtopic.getDescription(), subtopic.isStatus());

				List<Resource> subtopicResources = new ArrayList<>();
				for (Resource r : allResources) {
					if (r.getSubtopic() != null && r.getSubtopic().equals(subtopic)) {
						subtopicResources.add(r);
					}
				}
				subtopicWResources.setResources(subtopicResources);
				content.add(subtopicWResources);
			}
		}
		planContent.setContent(content);

		return planContent;
	}

	// DELETE
	public String deletePlan(int planId) {
		Optional<Plan> optPlan = planDao.findById(planId);
		if (optPlan.isEmpty()) {
			log.warn("Plan does not exist");
			throw new NoSuchElementException("Plan does not exist");
		}

		planDao.deleteById(planId);
		if (planDao.findById(planId).isPresent()) {
			log.warn("Plan cannot be deleted");
			return "Plan cannot be deleted";
		}
		log.info("Plan: {} was successfully deleted", optPlan.get().getName());
		return "Plan successfully deleted";
	}

}
