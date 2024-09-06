package com.example.p1_backend.controllers;

import com.example.p1_backend.models.Question;
import com.example.p1_backend.models.dtos.InQuestionDto;
import com.example.p1_backend.models.dtos.QuestionNoTopicNoUserDto;
import com.example.p1_backend.models.dtos.QuestionNoUserDto;
import com.example.p1_backend.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("questions")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class QuestionController {

	private final QuestionService qs;

	@Autowired
	public QuestionController(QuestionService qs) {
		this.qs = qs;
	}

	// CREATE
	@PostMapping("create/{topicId}")
	public ResponseEntity<Question> createQuestion(@RequestHeader("Authorization") String token,
			@PathVariable int topicId, @RequestBody InQuestionDto questionDto) throws AccountNotFoundException {
		Question question = qs.createQuestion(token, topicId, questionDto);
		return new ResponseEntity<>(question, CREATED);
	}

	// READ
	@GetMapping("{questionId}")
	public ResponseEntity<Question> readQuestion(@PathVariable int questionId) {
		Question question = qs.readQuestion(questionId);
		return new ResponseEntity<>(question, OK);
	}

	@GetMapping("user")
	public ResponseEntity<List<QuestionNoUserDto>> getQuestionsByUser(@RequestHeader("Authorization") String token)
			throws AccountNotFoundException {
		List<QuestionNoUserDto> questions = qs.getQuestionsByUser(token);
		return new ResponseEntity<>(questions, OK);
	}

	@GetMapping("/topic/{topicId}")
	public ResponseEntity<List<QuestionNoTopicNoUserDto>> getQuestionByTopic(@PathVariable int topicId) {
		List<QuestionNoTopicNoUserDto> questions = qs.getQuestionsByTopic(topicId);
		return new ResponseEntity<>(questions, OK);
	}

	@GetMapping("plan/{planId}")
	public ResponseEntity<List<QuestionNoUserDto>> getQuestionByPlan(@PathVariable int planId) {
		List<QuestionNoUserDto> questions = qs.getQuestionsByPlan(planId);
		return new ResponseEntity<>(questions, OK);
	}

	// UPDATE
	@PatchMapping("correct/{questionId}")
	public ResponseEntity<Question> updateQuestionCorrect(@PathVariable int questionId) {
		Question question = qs.updateQuestionCorrect(questionId);
		return new ResponseEntity<>(question, OK);
	}

	@PutMapping("{questionId}")
	public ResponseEntity<Question> updateQuestionContent(@PathVariable int questionId,
			@RequestBody InQuestionDto questionDto) {
		Question question = qs.updateQuestionContent(questionId, questionDto);
		return new ResponseEntity<>(question, OK);
	}

	// DELETE
	@DeleteMapping("{questionId}")
	public ResponseEntity<String> deleteQuestion(@PathVariable int questionId) {
		String message = qs.deleteQuestion(questionId);
		return new ResponseEntity<>(message, OK);
	}

}
