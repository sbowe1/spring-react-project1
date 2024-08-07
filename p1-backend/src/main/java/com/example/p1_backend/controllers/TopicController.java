package com.example.p1_backend.controllers;

import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.dtos.InTopicDto;
import com.example.p1_backend.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("topics")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class TopicController {

	private final TopicService ts;

	@Autowired
	public TopicController(TopicService ts) {
		this.ts = ts;
	}

	// CREATE
	@PostMapping("create/{planId}")
	public ResponseEntity<Topic> createTopic(@PathVariable int planId, @RequestBody InTopicDto topicDto) {
		Topic topic = ts.createTopic(planId, topicDto);
		return new ResponseEntity<>(topic, CREATED);
	}

	// READ
	@GetMapping("{topicId}")
	public ResponseEntity<Topic> readTopic(@PathVariable int topicId) {
		Topic topic = ts.readTopic(topicId);
		return new ResponseEntity<>(topic, OK);
	}

	// UPDATE
	@PatchMapping("complete/{topicId}")
	public ResponseEntity<Topic> completeTopic(@PathVariable int topicId) {
		Topic topic = ts.updateTopic(topicId);
		return new ResponseEntity<>(topic, OK);
	}

}
