package com.example.p1_backend.services;

import com.example.p1_backend.models.*;
import com.example.p1_backend.repositories.*;
import com.example.p1_backend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.security.auth.login.AccountNotFoundException;
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

		Plan newPlan = planDao.save(new Plan(name, optUser.get()));
		return newPlan;
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
