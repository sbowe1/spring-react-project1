package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.p1_backend.models.Plan;
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

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

	@Mock
	private QuestionDao questionDao;

	@Mock
	private UserDao userDao;

	@Mock
	private PlanDao planDao;

	@Mock
	private TopicDao topicDao;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private QuestionService qs;

	/**
	 * Creates a mock user
	 * @return User
	 */
	public User getUser() {
		User user = new User("test-user-email@test.com", "test-user-password", "test-user-name", "ROLE_USER",
				"Spring Boot Roadmap");
		user.setUserId(1);
		return user;
	}

	/**
	 * Creates a mock topic
	 * @return Topic
	 */
	public Topic getTopic() {
		Plan plan = new Plan(1, "Spring Boot Roadmap", getUser());
		return new Topic(1, "Topic 1", "Description", plan, false);

	}

	/**
	 * Creates a mock question
	 * @return Question
	 */
	public Question getQuestion() {
		Question question = new Question(1, "question", "answer", false, getTopic());
		question.setUser(getUser());
		return question;
	}

	/**
	 * Creates a list of mock questions
	 * @return List<Question>
	 */
	public List<Question> getQuestionList() {
		List<Question> questions = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			questions.add(getQuestion());
		}
		return questions;
	}

	@DisplayName("Create Question")
	@Test
	public void createQuestion() throws AccountNotFoundException {
		String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";
		User mockUser = getUser();
		Question mockQuestion = getQuestion();
		Topic mockTopic = getTopic();
		InQuestionDto questionDto = new InQuestionDto("question", "answer");

		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(userDao.findById(anyInt())).thenReturn(Optional.of(mockUser));
		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));
		when(questionDao.save(any(Question.class))).thenReturn(mockQuestion);

		Question result = qs.createQuestion(mockToken, 1, questionDto);

		assertNotNull(result);
		assertEquals(1, result.getQuestionId());
		assertEquals("question", result.getQuestion());
		assertEquals("answer", result.getAnswer());
		assertFalse(result.isCorrect());
		assertEquals(mockTopic, result.getTopic());
		assertEquals(mockUser, result.getUser());
	}

	@DisplayName("Throw AccountNotFoundException if user not found")
	@Test
	public void createQuestionAccountNotFound() {
		String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";
		InQuestionDto questionDto = new InQuestionDto("question", "answer");

		when(jwtUtil.extractUserId(anyString())).thenReturn(1);
		when(userDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(AccountNotFoundException.class, () -> qs.createQuestion("Bearer " + mockToken, 1, questionDto));
		verify(jwtUtil, times(1)).extractUserId(mockToken);
		verify(userDao, times(1)).findById(1);
	}

	@DisplayName("Throw NoSuchElementException if topic not found")
	@Test
	public void createQuestionTopicNotFound() {
		String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";
		User mockUser = getUser();
		InQuestionDto questionDto = new InQuestionDto("question", "answer");

		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(userDao.findById(anyInt())).thenReturn(Optional.of(mockUser));
		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> qs.createQuestion("Bearer " + mockToken, 1, questionDto));
		verify(jwtUtil, times(1)).extractUserId(mockToken);
		verify(userDao, times(1)).findById(1);
		verify(topicDao, times(1)).findById(1);
	}

	@DisplayName("Return Question if exists")
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

	@DisplayName("Throw NoSuchElementException if Question does not exist")
	@Test
	public void readQuestionQuestionNotFound() {
		when(questionDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> qs.readQuestion(1));
		verify(questionDao, times(1)).findById(1);
	}

	@DisplayName("Return all user's questions if User exists")
	@Test
	public void getQuestionByUser() throws AccountNotFoundException {
		String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";
		List<Question> mockQuestionList = getQuestionList();
		User mockUser = getUser();

		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(userDao.findById(anyInt())).thenReturn(Optional.of(mockUser));
		when(questionDao.findAllByUserUserId(anyInt())).thenReturn(mockQuestionList);

		List<QuestionNoUserDto> result = qs.getQuestionsByUser("Bearer " + mockToken);

		assertNotNull(result);
		assertEquals(5, result.size());
		assertEquals(new QuestionNoUserDto(getQuestion()), result.get(0));
	}

	@DisplayName("Throw AccountNotFoundException if User not found")
	@Test
	public void getQuestionsByUserUserNotFound() {
		String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";

		when(jwtUtil.extractUserId(anyString())).thenReturn(1);
		when(userDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(AccountNotFoundException.class, () -> qs.getQuestionsByUser("Bearer " + mockToken));
		verify(jwtUtil, times(1)).extractUserId(mockToken);
		verify(userDao, times(1)).findById(anyInt());
	}

	@DisplayName("Return all questions by topic if Topic exists")
	@Test
	public void getQuestionsByTopic() {
		List<Question> mockQuestionList = getQuestionList();
		Topic mockTopic = getTopic();

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));
		when(questionDao.findAllByTopicTopicId(anyInt())).thenReturn(mockQuestionList);

		List<QuestionNoTopicNoUserDto> result = qs.getQuestionsByTopic(1);

		assertNotNull(result);
		assertEquals(5, result.size());
		assertEquals(new QuestionNoTopicNoUserDto(getQuestion()), result.get(0));
	}

	@DisplayName("Throw NoSuchElementException if Topic does not exist")
	@Test
	public void getQuestionsByTopicTopicNotFound() {
		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> qs.getQuestionsByTopic(1));
		verify(topicDao, times(1)).findById(1);
	}

	@DisplayName("Return all questions by plan if Plan exists")
	@Test
	public void getQuestionsByPlan() {
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap", getUser());
		List<Question> mockQuestionList = getQuestionList();
		List<QuestionNoUserDto> actualQuestionList = new ArrayList<>();
		for (Question q : getQuestionList()) {
			actualQuestionList.add(new QuestionNoUserDto(q));
		}

		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan));
		when(topicDao.findAllByPlanPlanId(anyInt())).thenReturn(List.of(getTopic()));
		when(questionDao.findAllByTopicTopicId(anyInt())).thenReturn(mockQuestionList);

		List<QuestionNoUserDto> result = qs.getQuestionsByPlan(1);

		assertNotNull(result);
		assertEquals(actualQuestionList, result);
	}

	@DisplayName("Throw NoSuchElementException if Plan does not exist")
	@Test
	public void getQuestionsByPlanPlanNotFound() {
		when(planDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> qs.getQuestionsByPlan(1));
		verify(planDao, times(1)).findById(1);
	}

	@DisplayName("Update question status to correct")
	@Test
	public void updateQuestionCorrect() {
		Question mockQuestion = getQuestion();
		User mockUser = getUser();
		Topic mockTopic = getTopic();
		Question updatedQuestion = getQuestion();
		updatedQuestion.setCorrect(!getQuestion().isCorrect());

		when(questionDao.findById(anyInt())).thenReturn(Optional.of(mockQuestion));
		when(questionDao.save(any(Question.class))).thenReturn(updatedQuestion);

		Question result = qs.updateQuestionCorrect(1);

		assertNotNull(result);
		assertEquals(1, result.getQuestionId());
		assertEquals("question", result.getQuestion());
		assertEquals("answer", result.getAnswer());
		assertEquals(!getQuestion().isCorrect(), result.isCorrect());
		assertEquals(mockTopic, result.getTopic());
		assertEquals(mockUser, result.getUser());
	}

	@DisplayName("Throw NoSuchElementException if Question does not exist")
	@Test
	public void updateQuestionCorrectQuestionNotFound() {
		when(questionDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> qs.updateQuestionCorrect(1));
		verify(questionDao, times(1)).findById(1);
	}

	@DisplayName("Update content of question")
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

	@DisplayName("Throw NoSuchElementException if Question does not exist")
	@Test
	public void updateQuestionContentQuestionNotFound() {
		InQuestionDto questionDto = new InQuestionDto();
		questionDto.setAnswer("new answer");

		when(questionDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> qs.updateQuestionContent(1, questionDto));
		verify(questionDao, times(1)).findById(1);
	}

	@DisplayName("Delete Question if exists")
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

	@DisplayName("Throw NoSuchElementException if Question does not exist")
	@Test
	public void deleteQuestionQuestionNotFound() {
		when(questionDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> qs.deleteQuestion(1));
		verify(questionDao, times(1)).findById(1);
	}

	@DisplayName("Throw NoSuchElementException if Question failed to delete")
	@Test
	public void deleteQuestionFailedToDelete() {
		Question mockQuestion = getQuestion();

		when(questionDao.findById(anyInt())).thenReturn(Optional.of(mockQuestion))
			.thenReturn(Optional.of(mockQuestion));

		String result = qs.deleteQuestion(1);

		assertEquals("Could not delete Question: " + mockQuestion.getQuestion(), result);
		verify(questionDao, times(2)).findById(1);
	}

}
