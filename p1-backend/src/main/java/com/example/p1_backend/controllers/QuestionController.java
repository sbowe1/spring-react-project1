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
	 * @param token The user's Authorization token
	 * @param topicId The ID of the topic associated with the question
	 * @param questionDto DTO consisting of: question, answer
	 * @return The newly created question
	 * @throws AccountNotFoundException If the user is not found
	 */
	@PostMapping("create/{topicId}")
	public ResponseEntity<Question> createQuestion(@RequestHeader("Authorization") String token,
			@PathVariable int topicId, @RequestBody InQuestionDto questionDto) throws AccountNotFoundException {
		Question question = qs.createQuestion(token, topicId, questionDto);
		return new ResponseEntity<>(question, CREATED);
	}

	/**
	 * Reads a question.
	 * @param questionId The ID of the desired question
	 * @return Question
	 */
	@GetMapping("{questionId}")
	public ResponseEntity<Question> readQuestion(@PathVariable int questionId) {
		Question question = qs.readQuestion(questionId);
		return new ResponseEntity<>(question, OK);
	}

	/**
	 * Reads all questions created by a user.
	 * @return List<Question>
	 * @throws AccountNotFoundException If the user is not found
	 */
	@GetMapping("user")
	public ResponseEntity<List<QuestionNoUserDto>> getQuestionsByUser(@RequestHeader("Authorization") String token)
			throws AccountNotFoundException {
		List<QuestionNoUserDto> questions = qs.getQuestionsByUser(token);
		return new ResponseEntity<>(questions, OK);
	}

	/**
	 * Reads all questions in a topic.
	 * @param topicId The ID of the topic associated with the questions
	 * @return List<Question>
	 */
	@GetMapping("/topic/{topicId}")
	public ResponseEntity<List<QuestionNoTopicNoUserDto>> getQuestionByTopic(@PathVariable int topicId) {
		List<QuestionNoTopicNoUserDto> questions = qs.getQuestionsByTopic(topicId);
		return new ResponseEntity<>(questions, OK);
	}

	/**
	 * Reads all questions in a plan.
	 * @param planId The ID of the plan associated with the questions
	 * @return List of questions. Each question states which topic it belongs to in the
	 * plan.
	 */
	@GetMapping("plan/{planId}")
	public ResponseEntity<List<QuestionNoUserDto>> getQuestionByPlan(@PathVariable int planId) {
		List<QuestionNoUserDto> questions = qs.getQuestionsByPlan(planId);
		return new ResponseEntity<>(questions, OK);
	}

	/**
	 * Toggles a question's correctness status.
	 * @param questionId The ID of the question to be updated
	 * @return The updated question
	 */
	@PatchMapping("correct/{questionId}")
	public ResponseEntity<Question> updateQuestionCorrect(@PathVariable int questionId) {
		Question question = qs.updateQuestionCorrect(questionId);
		return new ResponseEntity<>(question, OK);
	}

	/**
	 * Updates a question's content (either the question itself, or the answer).
	 * @param questionId The ID of the question to be updated
	 * @param questionDto A question object including all the fields to be updated
	 * @return The updated question
	 */
	@PutMapping("{questionId}")
	public ResponseEntity<Question> updateQuestionContent(@PathVariable int questionId,
			@RequestBody InQuestionDto questionDto) {
		Question question = qs.updateQuestionContent(questionId, questionDto);
		return new ResponseEntity<>(question, OK);
	}

	/**
	 * Deletes a question.
	 * @param questionId The ID of the question to be deleted
	 * @return Successful deletion message; Failure message if error occurs
	 */
	@DeleteMapping("{questionId}")
	public ResponseEntity<String> deleteQuestion(@PathVariable int questionId) {
		String message = qs.deleteQuestion(questionId);
		return new ResponseEntity<>(message, OK);
	}

}
