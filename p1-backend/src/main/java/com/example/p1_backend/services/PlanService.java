package com.example.p1_backend.services;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.dtos.OutPlan;
import com.example.p1_backend.repositories.PlanDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class PlanService {

	private final PlanDao planDao;

	@Autowired
	public PlanService(PlanDao planDao) {
		this.planDao = planDao;
	}

	// CREATE
	public Plan createPlan(String name) {
		Optional<Plan> optPlan = planDao.getByName(name);
		if (optPlan.isPresent()) {
			log.warn("That plan already exists");
			throw new KeyAlreadyExistsException("That plan already exists");
		}

		Plan newPlan = planDao.save(new Plan(name));
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
	public OutPlan readContents(int planId) {
		Optional<Plan> optPlan = planDao.findById(planId);
		if (optPlan.isEmpty()) {
			log.warn("Plan does not exist");
			throw new NoSuchElementException("Plan does not exist");
		}

		// TODO: retrieve all topics, subtopics, and resources associated with plan
		OutPlan planContents = new OutPlan();
		planContents.setPlanId(optPlan.get().getPlanId());
		planContents.setName(optPlan.get().getName());

		return planContents;
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
