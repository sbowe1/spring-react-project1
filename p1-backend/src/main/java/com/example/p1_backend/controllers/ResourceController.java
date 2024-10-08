package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.p1_backend.models.dtos.InResourceDto;
import com.example.p1_backend.models.dtos.OutResourceDto;
import com.example.p1_backend.services.ResourceService;

@RestController
@RequestMapping("resources")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class ResourceController {

	private final ResourceService rs;

	@Autowired
	public ResourceController(ResourceService rs) {
		this.rs = rs;
	}

	/**
	 * Creates a new resource on the topic level. Resources created with this method will
	 * have no subtopic associated with them.
	 * @param topicId The ID of the topic associated with the new resource
	 * @param resourceDto DTO consisting of: resource title, description, type, URL
	 * (optional)
	 * @return Resource, including its relevant topic
	 */
	@PostMapping("{topicId}")
	public ResponseEntity<OutResourceDto> createNoSubtopic(@PathVariable int topicId,
			@RequestBody InResourceDto resourceDto) {
		OutResourceDto resource = rs.createResourceNoSubtopic(topicId, resourceDto);
		return new ResponseEntity<>(resource, CREATED);
	}

	/**
	 * Creates a new resource on the subtopic level. Resources created with this method
	 * will also have a topic associated with them.
	 * @param topicId The ID of the topic associated with the resource
	 * @param subtopicId The ID of the subtopic associated with the resource
	 * @param resourceDto DTO consisting of: resource title, description, type, URL
	 * (optional)
	 * @return Resource, including its relevant topic and subtopic
	 */
	@PostMapping("{topicId}/{subtopicId}")
	public ResponseEntity<OutResourceDto> createWithSubtopic(@PathVariable int topicId, @PathVariable int subtopicId,
			@RequestBody InResourceDto resourceDto) {
		OutResourceDto resource = rs.createResourceSubtopic(topicId, subtopicId, resourceDto);
		return new ResponseEntity<>(resource, CREATED);
	}

	/**
	 * Reads a resource by its ID.
	 * @param resourceId The ID of the desired resource
	 * @return Resource
	 */
	@GetMapping("{resourceId}")
	public ResponseEntity<OutResourceDto> readResource(@PathVariable int resourceId) {
		OutResourceDto resource = rs.readResource(resourceId);
		return new ResponseEntity<>(resource, OK);
	}

	/**
	 * Updates a resource's content.
	 * @param resourceId The ID of the resource to be updated
	 * @param inResourceDto A resource object consisting of all the fields to be updated
	 * @return The updated resource
	 */
	@PatchMapping("{resourceId}")
	public ResponseEntity<OutResourceDto> updateResoure(@PathVariable int resourceId,
			@RequestBody InResourceDto inResourceDto) {
		OutResourceDto resource = rs.updateResource(resourceId, inResourceDto);
		return new ResponseEntity<>(resource, OK);
	}

}
