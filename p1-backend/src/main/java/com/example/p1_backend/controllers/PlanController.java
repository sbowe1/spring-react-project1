package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.dtos.PlanContent;
import com.example.p1_backend.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("plans")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class PlanController {

	private final PlanService ps;

	@Autowired
	public PlanController(PlanService ps) {
		this.ps = ps;
	}

	// CREATE
	@PostMapping("create")
	public ResponseEntity<Plan> createPlan(@RequestHeader("Authorization") String token, @RequestBody String name)
			throws AccountNotFoundException {
		Plan plan = ps.createPlan(token, name);
		return new ResponseEntity<>(plan, CREATED);
	}

	// READ PLAN
	@GetMapping("{planId}")
	public ResponseEntity<Plan> readPlan(@PathVariable int planId) {
		Plan plan = ps.readPlan(planId);
		return new ResponseEntity<>(plan, OK);
	}

	// READ CONTENTS
	@GetMapping("content/{planId}")
	public ResponseEntity<PlanContent> readPlanContents(@RequestHeader("Authorization") String token,
			@PathVariable int planId) {
		PlanContent plan = ps.readPlanContents(token, planId);
		return new ResponseEntity<>(plan, OK);
	}

	// DELETE
	@DeleteMapping("{planId}")
	public ResponseEntity<String> deletePlan(@PathVariable int planId) {
		String message = ps.deletePlan(planId);
		return new ResponseEntity<>(message, OK);
	}

}
