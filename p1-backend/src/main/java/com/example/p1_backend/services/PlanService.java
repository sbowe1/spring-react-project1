package com.example.p1_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.Resource;
import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.PlanContent;
import com.example.p1_backend.models.dtos.SubtopicWResources;
import com.example.p1_backend.models.dtos.TopicWResources;
import com.example.p1_backend.repositories.PlanDao;
import com.example.p1_backend.repositories.ResourceDao;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;
import com.example.p1_backend.repositories.UserDao;
import com.example.p1_backend.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

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

	/**
	 * Creates a new plan.
	 * @param token The user's Authorization token
	 * @param name The name of the new plan
	 * @return The newly created plan
	 * @throws AccountNotFoundException If the user is not found
	 */
	public Plan createPlan(String token, String name) throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));

		Optional<User> optUser = userDao.findById(userId);
		if (optUser.isEmpty()) {
			throw new AccountNotFoundException("User with userId: " + userId + " not found");
		}

		// Ensuring user doesn't already have a plan with that name
		Optional<Plan> optPlan = planDao.getByNameAndUserUserId(name, userId);
		if (optPlan.isPresent()) {
			log.warn("That plan already exists");
			throw new KeyAlreadyExistsException("That plan already exists");
		}

		log.info("Plan: {} created successfully", name);
		Plan plan = planDao.save(new Plan(name, optUser.get()));

		// After creating plan, add it to User's profile
		optUser.get().getPlans().add(plan.getName());
		optUser.get().setPlans(optUser.get().getPlans());
		userDao.save(optUser.get());

		return plan;
	}

	/**
	 * Reads a plan by its ID.
	 * @param planId The ID of the plan to search for in the database
	 * @return Plan
	 */
	public Plan readPlan(int planId) {
		Optional<Plan> optPlan = planDao.findById(planId);
		if (optPlan.isEmpty()) {
			log.warn("Plan does not exist");
			throw new NoSuchElementException("Plan does not exist");
		}

		return optPlan.get();
	}

	/**
	 * Reads a plan's contents. The contents are a list of all topics, subtopics, and
	 * resources associated with the plan. Topics and its resources are listed first,
	 * followed by the topic's subtopics and their resources. This pattern is repeated
	 * until all topics and subtopics in the plan have been covered.
	 * @param token The user's Authorization token
	 * @param planId The ID of the plan to search for in the database
	 * @return Plan with contents
	 */
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

	/**
	 * Deletes a plan by its ID.
	 * @param planId The ID of the plan to be deleted
	 * @return Successful deletion message; Failure message if an error occurs
	 * @throws AccountNotFoundException If the user is not found
	 */
	public String deletePlan(int planId) throws AccountNotFoundException {
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

		Optional<User> optUser = userDao.findById(optPlan.get().getUser().getUserId());
		if (optUser.isEmpty()) {
			throw new AccountNotFoundException("User with userId: " + optUser.get().getUserId() + " not found");
		}
		List<String> plans = optUser.get().getPlans();
		plans.remove(optPlan.get().getName());
		optUser.get().setPlans(plans);
		userDao.save(optUser.get());

		return "Plan successfully deleted";
	}

}
