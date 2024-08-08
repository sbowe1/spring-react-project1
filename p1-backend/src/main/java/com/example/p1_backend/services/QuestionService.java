package com.example.p1_backend.services;

import com.example.p1_backend.models.Question;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InQuestionDto;
import com.example.p1_backend.models.dtos.QuestionNoTopicNoUserDto;
import com.example.p1_backend.models.dtos.QuestionNoUserDto;
import com.example.p1_backend.repositories.QuestionDao;
import com.example.p1_backend.repositories.TopicDao;
import com.example.p1_backend.repositories.UserDao;
import com.example.p1_backend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class QuestionService {

	private final QuestionDao questionDao;

	private final UserDao userDao;

	private final TopicDao topicDao;

	private final JwtUtil jwtUtil;

	@Autowired
	public QuestionService(QuestionDao questionDao, UserDao userDao, TopicDao topicDao, JwtUtil jwtUtil) {
		this.questionDao = questionDao;
		this.userDao = userDao;
		this.topicDao = topicDao;
		this.jwtUtil = jwtUtil;
	}

	// CREATE
	public Question createQuestion(String token, int topicId, InQuestionDto questionDto) {
		int userId = jwtUtil.extractUserId(token.substring(7));
		Optional<User> optUser = userDao.findById(userId);
		Optional<Topic> optTopic = topicDao.findById(topicId);

		Question question = new Question(questionDto.getQuestion(), questionDto.getAnswer(), false,
				optTopic.orElse(null), optUser.orElse(null));

		log.info("Question: {} created successfully", question.getQuestion());
		return questionDao.save(question);
	}

	// READ
	public Question readQuestion(int questionId) {
		Optional<Question> optQuestion = questionDao.findById(questionId);
		if (optQuestion.isEmpty()) {
			log.warn("Question does not exist");
			throw new NoSuchElementException("Question does not exist");
		}

		return optQuestion.get();
	}

	public List<QuestionNoUserDto> getQuestionsByUser(String token) {
		int userId = jwtUtil.extractUserId(token.substring(7));
		List<Question> questions = questionDao.findAllByUserUserId(userId);

		List<QuestionNoUserDto> questionList = new ArrayList<>();
		for (Question question : questions) {
			QuestionNoUserDto q = new QuestionNoUserDto(question);
			questionList.add(q);
		}

		return questionList;
	}

	public List<QuestionNoTopicNoUserDto> getQuestionsByTopic(int topicId) {
		List<Question> questions = questionDao.findAllByTopicTopicId(topicId);

		List<QuestionNoTopicNoUserDto> questionList = new ArrayList<>();
		for (Question question : questions) {
			QuestionNoTopicNoUserDto q = new QuestionNoTopicNoUserDto(question);
			questionList.add(q);
		}

		return questionList;
	}

	public List<QuestionNoUserDto> getQuestionsByPlan(int planId) {
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

	// UPDATE
	public Question updateQuestionCorrect(int questionId) {
		Optional<Question> optQuestion = questionDao.findById(questionId);
		if (optQuestion.isEmpty()) {
			log.warn("Question does not exist");
			throw new NoSuchElementException("Question does not exist");
		}

		optQuestion.get().setCorrect(true);
		log.info("Status set to true");
		return questionDao.save(optQuestion.get());
	}

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

	// DELETE
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
