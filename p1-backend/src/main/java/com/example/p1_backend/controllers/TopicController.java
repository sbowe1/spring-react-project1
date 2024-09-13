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

import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.dtos.InTopicDto;
import com.example.p1_backend.services.TopicService;

@RestController
@RequestMapping("topics")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class TopicController {

	private final TopicService ts;

	@Autowired
	public TopicController(TopicService ts) {
		this.ts = ts;
	}

	/**
	 * Creates a new topic.
	 * @param planId
	 * @param topicDto
	 * @return Topic
	 */
	@PostMapping("create/{planId}")
	public ResponseEntity<Topic> createTopic(@PathVariable int planId, @RequestBody InTopicDto topicDto) {
		Topic topic = ts.createTopic(planId, topicDto);
		return new ResponseEntity<>(topic, CREATED);
	}

	/**
	 * Reads a topic by id.
	 * @param topicId
	 * @return Topic
	 */
	@GetMapping("{topicId}")
	public ResponseEntity<Topic> readTopic(@PathVariable int topicId) {
		Topic topic = ts.findByTopicId(topicId);
		return new ResponseEntity<>(topic, OK);
	}

	/**
	 * Completes a topic.
	 * @param topicId
	 * @return Topic
	 */
	@PatchMapping("complete/{topicId}")
	public ResponseEntity<Topic> completeTopic(@PathVariable int topicId) {
		Topic topic = ts.update(topicId);
		return new ResponseEntity<>(topic, OK);
	}

}
