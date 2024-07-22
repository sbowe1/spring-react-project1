package com.example.p1_backend.controllers;

import com.example.p1_backend.models.Question;
import com.example.p1_backend.models.dtos.InQuestionDto;
import com.example.p1_backend.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	@PostMapping("create")
	public ResponseEntity<Question> createQuestion(@RequestHeader("Authorization") String token,
			@RequestBody InQuestionDto questionDto) {
		Question question = qs.createQuestion(token, questionDto);
		return new ResponseEntity<>(question, CREATED);
	}

	// READ
	@GetMapping("{questionId}")
	public ResponseEntity<Question> readQuestion(@RequestHeader("Authorization") String token,
			@PathVariable int questionId) {
		Question question = qs.readQuestion(questionId);
		return new ResponseEntity<>(question, OK);
	}

	// UPDATE
	@PatchMapping("correct/{questionId}")
	public ResponseEntity<Question> updateQuestionCorrect(@RequestHeader("Authorization") String token,
			@PathVariable int questionId) {
		Question question = qs.updateQuestionCorrect(questionId);
		return new ResponseEntity<>(question, OK);
	}

	@PutMapping("{questionId}")
	public ResponseEntity<Question> updateQuestionContent(@RequestHeader("Authorization") String token,
			@PathVariable int questionId, @RequestBody InQuestionDto questionDto) {
		Question question = qs.updateQuestionContent(questionId, questionDto);
		return new ResponseEntity<>(question, OK);
	}

	// DELETE
	@DeleteMapping("{questionId}")
	public ResponseEntity<String> deleteQuestion(@RequestHeader("Authorization") String token,
			@PathVariable int questionId) {
		String message = qs.deleteQuestion(questionId);
		return new ResponseEntity<>(message, OK);
	}

}
