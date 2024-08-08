package com.example.p1_backend.controllers;

import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.dtos.InSubtopicDto;
import com.example.p1_backend.services.SubtopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("subtopics")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class SubtopicController {

	private final SubtopicService ss;

	@Autowired
	public SubtopicController(SubtopicService ss) {
		this.ss = ss;
	}

	// CREATE
	@PostMapping("create/{topicId}")
	public ResponseEntity<Subtopic> createSubtopic(@PathVariable int topicId, @RequestBody InSubtopicDto subtopicDto) {
		Subtopic subtopic = ss.createSubtopic(topicId, subtopicDto);
		return new ResponseEntity<>(subtopic, CREATED);
	}

	// READ
	@GetMapping("{subtopicId}")
	public ResponseEntity<Subtopic> readSubtopic(@PathVariable int subtopicId) {
		Subtopic subtopic = ss.readSubtopic(subtopicId);
		return new ResponseEntity<>(subtopic, OK);
	}

	// UPDATE
	@PatchMapping("complete/{subtopicId}")
	public ResponseEntity<Subtopic> updateSubtopic(@PathVariable int subtopicId) {
		Subtopic subtopic = ss.updateSubtopic(subtopicId);
		return new ResponseEntity<>(subtopic, OK);
	}

}
