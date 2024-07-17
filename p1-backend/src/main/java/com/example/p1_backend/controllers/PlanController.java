package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.dtos.OutPlan;
import com.example.p1_backend.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<Plan> createPlan(@RequestHeader("Authorization") String token, @RequestBody String name) {
		Plan plan = ps.createPlan(name);
		return new ResponseEntity<>(plan, CREATED);
	}

	// READ PLAN
	@GetMapping("{planId}")
	public ResponseEntity<Plan> readPlan(@RequestHeader("Authorization") String token, @PathVariable int planId) {
		Plan plan = ps.readPlan(planId);
		return new ResponseEntity<>(plan, OK);
	}

	// READ CONTENTS
	@GetMapping("contents/{planId}")
	public ResponseEntity<OutPlan> readContents(@RequestHeader("Authorization") String token,
			@PathVariable int planId) {
		OutPlan planWithContents = ps.readContents(planId);
		return new ResponseEntity<>(planWithContents, OK);
	}

	// DELETE
	@DeleteMapping("{planId}")
	public ResponseEntity<String> deletePlan(@RequestHeader("Authorization") String token, @PathVariable int planId) {
		String message = ps.deletePlan(planId);
		return new ResponseEntity<>(message, OK);
	}

}
