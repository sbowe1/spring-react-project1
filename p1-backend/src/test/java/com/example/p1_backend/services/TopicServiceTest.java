package com.example.p1_backend.services;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InTopicDto;
import com.example.p1_backend.repositories.PlanDao;
import com.example.p1_backend.repositories.TopicDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

	@Mock
	private TopicDao topicDao;

	@Mock
	private PlanDao planDao;

	@InjectMocks
	private TopicService ts;

	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	public Plan getPlan() {
		return new Plan(1, "Spring Boot Roadmap", getMockUser());
	}

	public Topic getTopic() {
		Plan plan = getPlan();
		Topic topic = new Topic("Topic 1", "Description", plan, false);
		topic.setTopicId(1);
		return topic;
	}

	// CREATE
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

	@Test
	public void createTopicPlanNotFound() {
		InTopicDto topicDto = new InTopicDto("Topic 1", "Description");

		when(planDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ts.createTopic(1, topicDto));
		verify(planDao, times(1)).findById(1);
	}

	// READ
	@Test
	public void readTopic() {
		Topic mockTopic = getTopic();

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));

		Topic result = ts.readTopic(1);

		assertNotNull(result);
		assertEquals(1, result.getTopicId());
		assertEquals("Topic 1", result.getTitle());
		assertEquals(mockTopic.getPlan(), result.getPlan());
		assertFalse(result.isStatus());
	}

	@Test
	public void readTopicTopicNotFound() {
		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ts.readTopic(1));
		verify(topicDao, times(1)).findById(1);
	}

	// UPDATE
	@Test
	public void updateTopic() {
		Topic mockTopic = getTopic();
		Topic updatedTopic = getTopic();
		updatedTopic.setStatus(true);

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));
		when(topicDao.save(any(Topic.class))).thenReturn(updatedTopic);

		Topic result = ts.updateTopic(1);

		assertNotNull(result);
		assertEquals(1, result.getTopicId());
		assertEquals("Topic 1", result.getTitle());
		assertEquals(mockTopic.getPlan(), result.getPlan());
		assertTrue(result.isStatus());
	}

	@Test
	public void updateTopicTopicNotFound() {
		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ts.updateTopic(1));
		verify(topicDao, times(1)).findById(1);
	}

}
