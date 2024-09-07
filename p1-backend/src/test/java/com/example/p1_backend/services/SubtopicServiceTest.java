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
import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InSubtopicDto;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;

@ExtendWith(MockitoExtension.class)
public class SubtopicServiceTest {

	@Mock
	private SubtopicDao subtopicDao;

	@Mock
	private TopicDao topicDao;

	@InjectMocks
	private SubtopicService ss;

	/**
	 * Creates a mock user
	 * @return User
	 */
	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	/**
	 * Creates a mock Plan object
	 * @return Plan
	 */
	public Plan getPlan() {
		return new Plan(1, "Spring Boot Roadmap", getMockUser());
	}

	/**
	 * Creates a mock Topic object
	 * @return Topic
	 */
	public Topic getTopic() {
		Plan plan = getPlan();
		return new Topic(1, "Topic 1", "Description", plan, false);
	}

	/**
	 * Creates a mock Subtopic object
	 * @return Subtopic
	 */
	public Subtopic getSubtopic() {
		Topic topic = getTopic();
		return new Subtopic(1, "Subtopic 1", "Description", topic, false);
	}

	@DisplayName("Return new Subtopic if its parent Topic exists")
	@Test
	public void create() {
		InSubtopicDto subtopicDto = new InSubtopicDto("Subtopic 1", "Description");
		Topic mockTopic = getTopic();
		Subtopic mockSubtopic = new Subtopic(subtopicDto.getTitle(), subtopicDto.getDescription(), mockTopic, false);
		mockSubtopic.setSubtopicId(1);

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));
		when(subtopicDao.save(any(Subtopic.class))).thenReturn(mockSubtopic);

		Subtopic result = ss.create(1, subtopicDto);

		assertNotNull(result);
		assertEquals(1, result.getSubtopicId());
		assertEquals("Subtopic 1", result.getTitle());
		assertEquals("Description", result.getDescription());
		assertEquals(mockTopic, result.getTopic());
		assertFalse(result.isStatus());
	}

	@DisplayName("Throw NoSuchElementException if parent Topic does not exist")
	@Test
	public void createTopicNotFound() {
		InSubtopicDto subtopicDto = new InSubtopicDto("Subtopic 1", "Description");

		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ss.create(1, subtopicDto));
		verify(topicDao, times(1)).findById(1);
	}

	@DisplayName("Return Subtopic if it exists")
	@Test
	public void readSubtopic() {
		Subtopic mockSubtopic = getSubtopic();
		mockSubtopic.setSubtopicId(1);

		when(subtopicDao.findById(anyInt())).thenReturn(Optional.of(mockSubtopic));

		Subtopic result = ss.readSubtopic(1);

		assertNotNull(result);
		assertEquals(1, result.getSubtopicId());
		assertEquals("Subtopic 1", result.getTitle());
		assertEquals("Description", result.getDescription());
		assertEquals(getTopic(), result.getTopic());
		assertFalse(result.isStatus());
	}

	@DisplayName("Throw NoSuchElementException if Subtopic does not exist")
	@Test
	public void readSubtopicSubtopicNotFound() {
		when(subtopicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ss.readSubtopic(1));
		verify(subtopicDao, times(1)).findById(1);
	}

	@DisplayName("Update Subtopic status if exists")
	@Test
	public void updateSubtopic() {
		Subtopic mockSubtopic = getSubtopic();
		mockSubtopic.setSubtopicId(1);
		Subtopic updatedSubtopic = getSubtopic();
		updatedSubtopic.setSubtopicId(1);
		updatedSubtopic.setStatus(true);

		when(subtopicDao.findById(anyInt())).thenReturn(Optional.of(mockSubtopic));
		when(subtopicDao.save(any(Subtopic.class))).thenReturn(updatedSubtopic);

		Subtopic result = ss.updateSubtopic(1);

		assertNotNull(result);
		assertEquals(1, result.getSubtopicId());
		assertEquals("Subtopic 1", result.getTitle());
		assertEquals("Description", result.getDescription());
		assertEquals(getTopic(), result.getTopic());
		assertTrue(result.isStatus());
	}

	@DisplayName("Throw NoSuchElementException if Subtopic does not exist")
	@Test
	public void updateSubtopicSubtopicNotFound() {
		when(subtopicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ss.updateSubtopic(1));
		verify(subtopicDao, times(1)).findById(1);
	}

}
