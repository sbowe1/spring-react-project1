package com.example.p1_backend.services;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.Question;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InQuestionDto;
import com.example.p1_backend.repositories.QuestionDao;
import com.example.p1_backend.repositories.TopicDao;
import com.example.p1_backend.repositories.UserDao;
import com.example.p1_backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

	@Mock
	private QuestionDao questionDao;

	@Mock
	private UserDao userDao;

	@Mock
	private TopicDao topicDao;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private QuestionService qs;

	public User getUser() {
		User user = new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
		user.setUserId(1);
		return user;
	}

	public Topic getTopic() {
		Plan plan = new Plan(1, "Spring Boot Roadmap");
		return new Topic(1, "Topic 1", plan, false);

	}

	public Question getQuestion() {
		Question question = new Question(1, "question", "answer", false, getTopic());
		question.setUser(getUser());
		return question;
	}

	// CREATE
	@Test
	public void createQuestion() {
		String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";
		User mockUser = getUser();
		Question mockQuestion = getQuestion();
		Topic mockTopic = getTopic();
		InQuestionDto questionDto = new InQuestionDto("question", "answer", "Topic 1");

		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(userDao.findById(anyInt())).thenReturn(Optional.of(mockUser));
		when(topicDao.getByTitle(anyString())).thenReturn(Optional.of(mockTopic));
		when(questionDao.save(any(Question.class))).thenReturn(mockQuestion);

		Question result = qs.createQuestion(mockToken, questionDto);

		assertNotNull(result);
		assertEquals(1, result.getQuestionId());
		assertEquals("question", result.getQuestion());
		assertEquals("answer", result.getAnswer());
		assertFalse(result.isCorrect());
		assertEquals(mockTopic, result.getTopic());
		assertEquals(mockUser, result.getUser());
	}

	// READ
	@Test
	public void readQuestion() {
		Question mockQuestion = getQuestion();
		User mockUser = getUser();
		Topic mockTopic = getTopic();

		when(questionDao.findById(anyInt())).thenReturn(Optional.of(mockQuestion));

		Question result = qs.readQuestion(1);

		assertNotNull(result);
		assertEquals(1, result.getQuestionId());
		assertEquals("question", result.getQuestion());
		assertEquals("answer", result.getAnswer());
		assertFalse(result.isCorrect());
		assertEquals(mockTopic, result.getTopic());
		assertEquals(mockUser, result.getUser());
	}

	// UPDATE
	@Test
	public void updateQuestionCorrect() {
		Question mockQuestion = getQuestion();
		User mockUser = getUser();
		Topic mockTopic = getTopic();
		Question updatedQuestion = getQuestion();
		updatedQuestion.setCorrect(true);

		when(questionDao.findById(anyInt())).thenReturn(Optional.of(mockQuestion));
		when(questionDao.save(any(Question.class))).thenReturn(updatedQuestion);

		Question result = qs.updateQuestionCorrect(1);

		assertNotNull(result);
		assertEquals(1, result.getQuestionId());
		assertEquals("question", result.getQuestion());
		assertEquals("answer", result.getAnswer());
		assertTrue(result.isCorrect());
		assertEquals(mockTopic, result.getTopic());
		assertEquals(mockUser, result.getUser());
	}

	@Test
	public void updateQuestionContent() {
		Question mockQuestion = getQuestion();
		User mockUser = getUser();
		Topic mockTopic = getTopic();
		Question updatedQuestion = getQuestion();
		updatedQuestion.setAnswer("new answer");
		InQuestionDto questionDto = new InQuestionDto();
		questionDto.setAnswer("new answer");

		when(questionDao.findById(anyInt())).thenReturn(Optional.of(mockQuestion));
		when(questionDao.save(any(Question.class))).thenReturn(updatedQuestion);

		Question result = qs.updateQuestionContent(1, questionDto);

		assertNotNull(result);
		assertEquals(1, result.getQuestionId());
		assertEquals("question", result.getQuestion());
		assertEquals("new answer", result.getAnswer());
		assertFalse(result.isCorrect());
		assertEquals(mockTopic, result.getTopic());
		assertEquals(mockUser, result.getUser());
	}

	// DELETE
	@Test
	public void deleteQuestion() {
		Question mockQuestion = getQuestion();

		when(questionDao.findById(anyInt())).thenReturn(Optional.of(mockQuestion)).thenReturn(Optional.empty());
		doNothing().when(questionDao).deleteById(anyInt());

		String result = qs.deleteQuestion(1);

		assertNotNull(result);
		assertEquals("Question: " + mockQuestion.getQuestion() + " deleted successfully", result);
		verify(questionDao, atMost(2)).findById(anyInt());
	}

}
