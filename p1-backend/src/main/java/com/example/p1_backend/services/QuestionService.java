package com.example.p1_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.p1_backend.models.Question;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InQuestionDto;
import com.example.p1_backend.models.dtos.QuestionNoTopicNoUserDto;
import com.example.p1_backend.models.dtos.QuestionNoUserDto;
import com.example.p1_backend.repositories.PlanDao;
import com.example.p1_backend.repositories.QuestionDao;
import com.example.p1_backend.repositories.TopicDao;
import com.example.p1_backend.repositories.UserDao;
import com.example.p1_backend.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionService {

	private final QuestionDao questionDao;

	private final UserDao userDao;

	private final PlanDao planDao;

	private final TopicDao topicDao;

	private final JwtUtil jwtUtil;

	@Autowired
	public QuestionService(QuestionDao questionDao, UserDao userDao, PlanDao planDao, TopicDao topicDao,
			JwtUtil jwtUtil) {
		this.questionDao = questionDao;
		this.userDao = userDao;
		this.planDao = planDao;
		this.topicDao = topicDao;
		this.jwtUtil = jwtUtil;
	}

	/**
	 * Creates a new question.
	 * @param token
	 * @param topicId
	 * @param questionDto
	 * @return Question
	 * @throws AccountNotFoundException
	 */
	public Question createQuestion(String token, int topicId, InQuestionDto questionDto)
			throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));
		Optional<User> optUser = userDao.findById(userId);
		if (optUser.isEmpty()) {
			log.warn("User does not exist");
			throw new AccountNotFoundException("User does not exist");
		}
		Optional<Topic> optTopic = topicDao.findById(topicId);
		if (optTopic.isEmpty()) {
			log.warn("Topic does not exist");
			throw new NoSuchElementException("Topic does not exist");
		}

		Question question = new Question(questionDto.getQuestion(), questionDto.getAnswer(), false,
				optTopic.orElse(null), optUser.orElse(null));

		log.info("Question: {} created successfully", question.getQuestion());
		return questionDao.save(question);
	}

	/**
	 * Reads a question by id.
	 * @param questionId
	 * @return Question
	 */
	public Question readQuestion(int questionId) {
		Optional<Question> optQuestion = questionDao.findById(questionId);
		if (optQuestion.isEmpty()) {
			log.warn("Question does not exist");
			throw new NoSuchElementException("Question does not exist");
		}

		return optQuestion.get();
	}

	/**
	 * Reads all questions made by a user.
	 * @param token
	 * @return List<QuestionNoUserDto>
	 * @throws AccountNotFoundException
	 */
	public List<QuestionNoUserDto> getQuestionsByUser(String token) throws AccountNotFoundException {
		int userId = jwtUtil.extractUserId(token.substring(7));
		if (userDao.findById(userId).isEmpty()) {
			log.warn("User does not exist");
			throw new AccountNotFoundException("User does not exist");
		}
		List<Question> questions = questionDao.findAllByUserUserId(userId);

		List<QuestionNoUserDto> questionList = new ArrayList<>();
		for (Question question : questions) {
			QuestionNoUserDto q = new QuestionNoUserDto(question);
			questionList.add(q);
		}

		return questionList;
	}

	/**
	 * Reads all questions by a topicId.
	 * @param topicId
	 * @return List<QuestionNoTopicNoUserDto>
	 */
	public List<QuestionNoTopicNoUserDto> getQuestionsByTopic(int topicId) {
		if (topicDao.findById(topicId).isEmpty()) {
			log.warn("Topic does not exist");
			throw new NoSuchElementException("Topic does not exist");
		}
		List<Question> questions = questionDao.findAllByTopicTopicId(topicId);

		List<QuestionNoTopicNoUserDto> questionList = new ArrayList<>();
		for (Question question : questions) {
			QuestionNoTopicNoUserDto q = new QuestionNoTopicNoUserDto(question);
			questionList.add(q);
		}

		return questionList;
	}

	/**
	 * Reads all questions by a planId.
	 * @param planId
	 * @return List<QuestionNoUserDto>
	 */
	public List<QuestionNoUserDto> getQuestionsByPlan(int planId) {
		if (planDao.findById(planId).isEmpty()) {
			log.warn("Plan does not exist");
			throw new NoSuchElementException("Plan does not exist");
		}
		List<QuestionNoUserDto> questions = new ArrayList<>();

		List<Topic> topicList = topicDao.findAllByPlanPlanId(planId);
		for (Topic topic : topicList) {
			List<Question> tempQuestionList = questionDao.findAllByTopicTopicId(topic.getTopicId());
			for (Question q : tempQuestionList) {
				questions.add(new QuestionNoUserDto(q));
			}
		}

		return questions;
	}

	/**
	 * Toggles a question's correct status.
	 * @param questionId
	 * @return Question
	 */
	public Question updateQuestionCorrect(int questionId) {
		Optional<Question> optQuestion = questionDao.findById(questionId);
		if (optQuestion.isEmpty()) {
			log.warn("Question does not exist");
			throw new NoSuchElementException("Question does not exist");
		}

		optQuestion.get().setCorrect(!optQuestion.get().isCorrect());
		log.info("Status set to " + optQuestion.get().isCorrect());
		return questionDao.save(optQuestion.get());
	}

	/**
	 * Updates a question's content.
	 * @param questionId
	 * @param questionDto
	 * @return Question
	 */
	public Question updateQuestionContent(int questionId, InQuestionDto questionDto) {
		Optional<Question> optQuestion = questionDao.findById(questionId);
		if (optQuestion.isEmpty()) {
			log.warn("Question does not exist");
			throw new NoSuchElementException("Question does not exist");
		}

		if (questionDto.getQuestion() != null && !questionDto.getQuestion().isEmpty()) {
			optQuestion.get().setQuestion(questionDto.getQuestion());
		}
		if (questionDto.getAnswer() != null && !questionDto.getAnswer().isEmpty()) {
			optQuestion.get().setAnswer(questionDto.getAnswer());
		}

		log.info("Question: {}'s contents updated", questionId);
		return questionDao.save(optQuestion.get());
	}

	/**
	 * Deletes a question by id.
	 * @param questionId
	 * @return String
	 */
	public String deleteQuestion(int questionId) {
		Optional<Question> optQuestion = questionDao.findById(questionId);
		if (optQuestion.isEmpty()) {
			log.warn("Question does not exist");
			throw new NoSuchElementException("Question does not exist");
		}

		questionDao.deleteById(questionId);

		if (questionDao.findById(questionId).isPresent()) {
			log.warn("Cannot delete question");
			return "Could not delete Question: " + optQuestion.get().getQuestion();
		}

		log.info("Question: {} successfully deleted", optQuestion.get().getQuestion());
		return "Question: " + optQuestion.get().getQuestion() + " deleted successfully";
	}

}
