package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.p1_backend.models.Plan;
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

}
