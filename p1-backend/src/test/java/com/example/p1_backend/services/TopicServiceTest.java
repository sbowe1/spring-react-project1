package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InTopicDto;
import com.example.p1_backend.repositories.PlanDao;
import com.example.p1_backend.repositories.TopicDao;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

	@Mock
	private TopicDao topicDao;

	@Mock
	private PlanDao planDao;

	@InjectMocks
	private TopicService ts;

	/**
	 * Creates a mock User object
	 * 
	 * @return User
	 */
	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	/**
	 * Creates a mock Plan object
	 * 
	 * @return Plan
	 */
	public Plan getPlan() {
		return new Plan(1, "Spring Boot Roadmap", getMockUser());
	}

	/**
	 * Creates a mock Topic object
	 * 
	 * @return Topic
	 */
	public Topic getTopic() {
		Plan plan = getPlan();
		Topic topic = new Topic("Topic 1", "Description", plan, false);
		topic.setTopicId(1);
		return topic;
	}

	@DisplayName("Return new Topic if its parent Plan exists")
	@Test
	public void createTopic() {
		InTopicDto topicDto = new InTopicDto("Topic 1", "Description");
		Plan mockPlan = getPlan();
		Topic mockTopic = new Topic(topicDto.getTopicName(), topicDto.getDescription(), mockPlan, false);
		mockTopic.setTopicId(1);

		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan));
		when(topicDao.save(any(Topic.class))).thenReturn(mockTopic);

		Topic result = ts.createTopic(1, topicDto);

		assertNotNull(result);
		assertEquals(1, result.getTopicId());
		assertEquals("Topic 1", result.getTitle());
		assertEquals("Description", result.getDescription());
		assertEquals(mockPlan, result.getPlan());
		assertFalse(result.isStatus());
	}

	@DisplayName("Throw NoSuchElementException if parent Plan does not exist")
	@Test
	public void createTopicPlanNotFound() {
		InTopicDto topicDto = new InTopicDto("Topic 1", "Description");

		when(planDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ts.createTopic(1, topicDto));
		verify(planDao, times(1)).findById(1);
	}

	@DisplayName("Return Topic if it exists")
	@Test
	public void findByTopicId() {
		Topic mockTopic = getTopic();

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));

		Topic result = ts.findByTopicId(1);

		assertNotNull(result);
		assertEquals(1, result.getTopicId());
		assertEquals("Topic 1", result.getTitle());
		assertEquals(mockTopic.getPlan(), result.getPlan());
		assertFalse(result.isStatus());
	}

	@DisplayName("Throw NoSuchElementException if Topic does not exist")
	@Test
	public void findByTopicIdTopicNotFound() {
		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ts.findByTopicId(1));
		verify(topicDao, times(1)).findById(1);
	}

	@DisplayName("Return updated topic if exists")
	@Test
	public void update() {
		Topic mockTopic = getTopic();
		Topic updatedTopic = getTopic();
		updatedTopic.setStatus(true);

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));
		when(topicDao.save(any(Topic.class))).thenReturn(updatedTopic);

		Topic result = ts.update(1);

		assertNotNull(result);
		assertEquals(1, result.getTopicId());
		assertEquals("Topic 1", result.getTitle());
		assertEquals(mockTopic.getPlan(), result.getPlan());
		assertTrue(result.isStatus());
	}

	@DisplayName("Throw NoSuchElementException if Topic does not exist")
	@Test
	public void updateTopicNotFound() {
		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ts.update(1));
		verify(topicDao, times(1)).findById(1);
	}

}
