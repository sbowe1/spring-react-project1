package com.example.p1_backend.services;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InSubtopicDto;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubtopicServiceTest {

	@Mock
	private SubtopicDao subtopicDao;

	@Mock
	private TopicDao topicDao;

	@InjectMocks
	private SubtopicService ss;

	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	public Plan getPlan() {
		return new Plan(1, "Spring Boot Roadmap", getMockUser());
	}

	public Topic getTopic() {
		Plan plan = getPlan();
		return new Topic(1, "Topic 1", "Description", plan, false);
	}

	public Subtopic getSubtopic() {
		Topic topic = getTopic();
		return new Subtopic(1, "Subtopic 1", "Description", topic, false);
	}

	// CREATE
	@Test
	public void createSubtopic() {
		InSubtopicDto subtopicDto = new InSubtopicDto("Subtopic 1", "Description");
		Topic mockTopic = getTopic();
		Subtopic mockSubtopic = new Subtopic(subtopicDto.getTitle(), subtopicDto.getDescription(), mockTopic, false);
		mockSubtopic.setSubtopicId(1);

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));
		when(subtopicDao.save(any(Subtopic.class))).thenReturn(mockSubtopic);

		Subtopic result = ss.createSubtopic(1, subtopicDto);

		assertNotNull(result);
		assertEquals(1, result.getSubtopicId());
		assertEquals("Subtopic 1", result.getTitle());
		assertEquals("Description", result.getDescription());
		assertEquals(mockTopic, result.getTopic());
		assertFalse(result.isStatus());
	}

	// READ
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

	// UPDATE
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

}
