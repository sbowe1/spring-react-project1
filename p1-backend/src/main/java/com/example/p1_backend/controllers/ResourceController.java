package com.example.p1_backend.controllers;

import com.example.p1_backend.models.dtos.InResourceDto;
import com.example.p1_backend.models.dtos.OutResourceDto;
import com.example.p1_backend.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("resources")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class ResourceController {

	private final ResourceService rs;

	@Autowired
	public ResourceController(ResourceService rs) {
		this.rs = rs;
	}

	// CREATE
	@PostMapping("{topicId}")
	public ResponseEntity<OutResourceDto> createNoSubtopic(@PathVariable int topicId,
			@RequestBody InResourceDto resourceDto) {
		OutResourceDto resource = rs.createResourceNoSubtopic(topicId, resourceDto);
		return new ResponseEntity<>(resource, CREATED);
	}

	@PostMapping("{topicId}/{subtopicId}")
	public ResponseEntity<OutResourceDto> createWithSubtopic(@PathVariable int topicId, @PathVariable int subtopicId,
			@RequestBody InResourceDto resourceDto) {
		OutResourceDto resource = rs.createResourceSubtopic(topicId, subtopicId, resourceDto);
		return new ResponseEntity<>(resource, CREATED);
	}

	// READ
	@GetMapping("{resourceId}")
	public ResponseEntity<OutResourceDto> readResource(@PathVariable int resourceId) {
		OutResourceDto resource = rs.readResource(resourceId);
		return new ResponseEntity<>(resource, OK);
	}

	// UPDATE
	@PatchMapping("{resourceId}")
	public ResponseEntity<OutResourceDto> updateResoure(@PathVariable int resourceId,
			@RequestBody InResourceDto inResourceDto) {
		OutResourceDto resource = rs.updateResource(resourceId, inResourceDto);
		return new ResponseEntity<>(resource, OK);
	}

}
