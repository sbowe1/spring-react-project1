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

import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.dtos.InSubtopicDto;
import com.example.p1_backend.services.SubtopicService;

@RestController
@RequestMapping("subtopics")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class SubtopicController {

	private final SubtopicService ss;

	@Autowired
	public SubtopicController(SubtopicService ss) {
		this.ss = ss;
	}

	/**
	 * Creates a new subtopic.
	 * @param topicId The ID of the topic associated with the new subtopic
	 * @param subtopicDto DTO consisting of: subtopic title, description
	 * @return Newly created subtopic
	 */
	@PostMapping("create/{topicId}")
	public ResponseEntity<Subtopic> create(@PathVariable int topicId, @RequestBody InSubtopicDto subtopicDto) {
		Subtopic subtopic = ss.create(topicId, subtopicDto);
		return new ResponseEntity<>(subtopic, CREATED);
	}

	/**
	 * Reads a subtopic by its ID.
	 * @param subtopicId The ID of the desired subtopic
	 * @return Subtopic
	 */
	@GetMapping("{subtopicId}")
	public ResponseEntity<Subtopic> readSubtopic(@PathVariable int subtopicId) {
		Subtopic subtopic = ss.readSubtopic(subtopicId);
		return new ResponseEntity<>(subtopic, OK);
	}

	/**
	 * Toggles the subtopic's completion status.
	 * @param subtopicId The ID of the subtopic to be updated
	 * @return The updated subtopic
	 */
	@PatchMapping("complete/{subtopicId}")
	public ResponseEntity<Subtopic> updateSubtopic(@PathVariable int subtopicId) {
		Subtopic subtopic = ss.updateSubtopic(subtopicId);
		return new ResponseEntity<>(subtopic, OK);
	}

}
