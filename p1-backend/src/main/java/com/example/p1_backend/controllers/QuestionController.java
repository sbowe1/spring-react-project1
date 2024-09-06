package com.example.p1_backend.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.p1_backend.models.Question;
import com.example.p1_backend.models.dtos.InQuestionDto;
import com.example.p1_backend.models.dtos.QuestionNoTopicNoUserDto;
import com.example.p1_backend.models.dtos.QuestionNoUserDto;
import com.example.p1_backend.services.QuestionService;

@RestController
@RequestMapping("questions")
@CrossOrigin(origins = { "http://localhost:3030 " }, allowCredentials = "true")
public class QuestionController {

	private final QuestionService qs;

	@Autowired
	public QuestionController(QuestionService qs) {
		this.qs = qs;
	}

	/**
	 * Creates a new question.
	 * @param	token
	 * @param	topicId
	 * @param	questionDto
	 * @return	Question
	 * @throws	AccountNotFoundException
	 */
	@PostMapping("create/{topicId}")
	public ResponseEntity<Question> createQuestion(@RequestHeader("Authorization") String token,
			@PathVariable int topicId, @RequestBody InQuestionDto questionDto) throws AccountNotFoundException {
		Question question = qs.createQuestion(token, topicId, questionDto);
		return new ResponseEntity<>(question, CREATED);
	}

	/**
	 * Reads a question.
	 * @param	questionId
	 * @return	Question
	 */
	@GetMapping("{questionId}")
	public ResponseEntity<Question> readQuestion(@PathVariable int questionId) {
		Question question = qs.readQuestion(questionId);
		return new ResponseEntity<>(question, OK);
	}

	/**
	 * Reads all questions.
	 * @return	List<QuestionNoUserDto>
	 */
	@GetMapping("user")
	public ResponseEntity<List<QuestionNoUserDto>> getQuestionsByUser(@RequestHeader("Authorization") String token)
			throws AccountNotFoundException {
		List<QuestionNoUserDto> questions = qs.getQuestionsByUser(token);
		return new ResponseEntity<>(questions, OK);
	}

	/**
	 * Reads all questions by topic.
	 * @param	topicId
	 * @return	List<QuestionNoTopicNoUserDto>
	 */
	@GetMapping("/topic/{topicId}")
	public ResponseEntity<List<QuestionNoTopicNoUserDto>> getQuestionByTopic(@PathVariable int topicId) {
		List<QuestionNoTopicNoUserDto> questions = qs.getQuestionsByTopic(topicId);
		return new ResponseEntity<>(questions, OK);
	}

	/**
	 * Reads all questions by plan.
	 * @param	planId
	 * @return	List<QuestionNoUserDto>
	 */
	@GetMapping("plan/{planId}")
	public ResponseEntity<List<QuestionNoUserDto>> getQuestionByPlan(@PathVariable int planId) {
		List<QuestionNoUserDto> questions = qs.getQuestionsByPlan(planId);
		return new ResponseEntity<>(questions, OK);
	}

	/**
	 * Updates a question to be correct.
	 * @param	questionId
	 * @return	Question
	 */
	@PatchMapping("correct/{questionId}")
	public ResponseEntity<Question> updateQuestionCorrect(@PathVariable int questionId) {
		Question question = qs.updateQuestionCorrect(questionId);
		return new ResponseEntity<>(question, OK);
	}

	/**
	 * Toggles a question's correct status.
	 * @param	questionId
	 * @return	Question
	 */
	@PutMapping("{questionId}")
	public ResponseEntity<Question> updateQuestionContent(@PathVariable int questionId,
			@RequestBody InQuestionDto questionDto) {
		Question question = qs.updateQuestionContent(questionId, questionDto);
		return new ResponseEntity<>(question, OK);
	}

	/**
	 * Deletes a question.
	 * @param	questionId
	 * @return	String
	 */
	@DeleteMapping("{questionId}")
	public ResponseEntity<String> deleteQuestion(@PathVariable int questionId) {
		String message = qs.deleteQuestion(questionId);
		return new ResponseEntity<>(message, OK);
	}

}
