package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.dtos.PlanContent;
import com.example.p1_backend.services.PlanService;

@RestController
@RequestMapping("plans")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class PlanController {

	private final PlanService ps;

	@Autowired
	public PlanController(PlanService ps) {
		this.ps = ps;
	}

	/**
	 * Creates a new plan.
	 * @param token
	 * @param name
	 * @return Plan
	 * @throws AccountNotFoundException
	 */
	@PostMapping("create")
	public ResponseEntity<Plan> createPlan(@RequestHeader("Authorization") String token, @RequestBody String name)
			throws AccountNotFoundException {
		Plan plan = ps.createPlan(token, name);
		return new ResponseEntity<>(plan, CREATED);
	}

	/**
	 * Reads a plan.
	 * @param planId
	 * @return Plan
	 */
	@GetMapping("{planId}")
	public ResponseEntity<Plan> readPlan(@PathVariable int planId) {
		Plan plan = ps.readPlan(planId);
		return new ResponseEntity<>(plan, OK);
	}

	/**
	 * Reads a plan's contents.
	 * @return PlanContent - Plan with List of its Topics and Subtopics
	 */
	@GetMapping("content/{planId}")
	public ResponseEntity<PlanContent> readPlanContents(@RequestHeader("Authorization") String token,
			@PathVariable int planId) {
		PlanContent plan = ps.readPlanContents(token, planId);
		return new ResponseEntity<>(plan, OK);
	}

	/**
	 * Deletes a plan.
	 * @param planId
	 * @return String
	 * @throws AccountNotFoundException
	 */
	@DeleteMapping("{planId}")
	public ResponseEntity<String> deletePlan(@PathVariable int planId) throws AccountNotFoundException {
		String message = ps.deletePlan(planId);
		return new ResponseEntity<>(message, OK);
	}

}
