package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.example.p1_backend.models.Resource;
import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.User;
import com.example.p1_backend.models.dtos.InResourceDto;
import com.example.p1_backend.models.dtos.OutResourceDto;
import com.example.p1_backend.repositories.ResourceDao;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

	@Mock
	private ResourceDao resourceDao;

	@Mock
	private TopicDao topicDao;

	@Mock
	private SubtopicDao subtopicDao;

	@InjectMocks
	private ResourceService rs;

	/**
	 * Creates a mock user
	 * @return User
	 */
	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-name", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	/**
	 * Creates a mock plan
	 * @return Plan
	 */
	public Plan getPlan() {
		return new Plan(1, "Spring Boot Roadmap", getMockUser());
	}

	/**
	 * Creates a mock topic
	 * @return Topic
	 */
	public Topic getTopic() {
		Plan plan = getPlan();
		return new Topic(1, "Topic 1", "Description", plan, false);
	}

	/**
	 * Creates a mock subtopic
	 * @return Subtopic
	 */
	public Subtopic getSubtopic() {
		Topic topic = getTopic();
		return new Subtopic(1, "Subtopic 1", "Description", topic, false);
	}

	/**
	 * Creates a mock resource DTO
	 * @return InResourceDto
	 */
	public InResourceDto getResourceDto() {
		return new InResourceDto("Resource Title", "Resource Description", "Resource Type", "Resource URL");
	}

	/**
	 * Creates a mock resource
	 * @return Resource
	 */
	public Resource getResource() {
		Topic topic = getTopic();
		Subtopic subtopic = getSubtopic();
		return new Resource(1, "Resource Title", "Resource Description", "Resource Type", "Resource URL", subtopic,
				topic);
	}

	@DisplayName("Create Resource with no Subtopic")
	@Test
	public void createResourceNoSubtopic() {
		InResourceDto mockResourceDto = getResourceDto();
		Resource mockResource = getResource();
		mockResource.setSubtopic(null);

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(getTopic()));
		when(resourceDao.save(any(Resource.class))).thenReturn(mockResource);

		OutResourceDto result = rs.createResourceNoSubtopic(getTopic().getTopicId(), mockResourceDto);

		assertNotNull(result);
		assertEquals(1, result.getResourceId());
		assertEquals("Resource Title", result.getTitle());
		assertEquals("Resource Description", result.getDescription());
		assertEquals("Resource Type", result.getType());
		assertEquals("Resource URL", result.getUrl());
		assertNull(result.getSubtopicName());
		assertEquals(getTopic().getTitle(), result.getTopicName());
	}

	@DisplayName("Throw NoSuchElementException if Topic does not exist")
	@Test
	public void createResourceNSTopicNotFound() {
		InResourceDto resourceDto = getResourceDto();

		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> rs.createResourceNoSubtopic(1, resourceDto));
		verify(topicDao, times(1)).findById(1);
	}

	@DisplayName("Create Resource with Subtopic if topic and subtopic exist")
	@Test
	public void createResourceSubtopic() {
		InResourceDto mockResourceDto = getResourceDto();
		Resource mockResource = getResource();

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(getTopic()));
		when(subtopicDao.findById(anyInt())).thenReturn(Optional.of(getSubtopic()));
		when(resourceDao.save(any(Resource.class))).thenReturn(mockResource);

		OutResourceDto result = rs.createResourceSubtopic(getTopic().getTopicId(), getSubtopic().getSubtopicId(),
				mockResourceDto);

		assertNotNull(result);
		assertEquals(1, result.getResourceId());
		assertEquals("Resource Title", result.getTitle());
		assertEquals("Resource Description", result.getDescription());
		assertEquals("Resource Type", result.getType());
		assertEquals("Resource URL", result.getUrl());
		assertEquals(getSubtopic().getTitle(), result.getSubtopicName());
		assertEquals(getTopic().getTitle(), result.getTopicName());
	}

	@DisplayName("Throw NoSuchElementException if Topic does not exist")
	@Test
	public void createResourceSubtopicTopicNotFound() {
		InResourceDto resourceDto = getResourceDto();

		when(topicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> rs.createResourceSubtopic(1, 1, resourceDto));
		verify(topicDao, times(1)).findById(1);
		verify(subtopicDao, times(0)).findById(1);
	}

	@DisplayName("Throw NoSuchElementException if Subtopic does not exist")
	@Test
	public void createResourceSubtopicSubtopicNotFound() {
		Topic mockTopic = getTopic();
		InResourceDto resourceDto = getResourceDto();

		when(topicDao.findById(anyInt())).thenReturn(Optional.of(mockTopic));
		when(subtopicDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> rs.createResourceSubtopic(1, 1, resourceDto));
		verify(topicDao, times(1)).findById(1);
		verify(subtopicDao, times(1)).findById(1);
	}

	@DisplayName("Return Resource if exists")
	@Test
	public void readResource() {
		Resource mockResource = getResource();

		when(resourceDao.findById(anyInt())).thenReturn(Optional.of(mockResource));

		OutResourceDto result = rs.readResource(1);

		assertNotNull(result);
		assertEquals(1, result.getResourceId());
		assertEquals("Resource Title", result.getTitle());
		assertEquals("Resource Description", result.getDescription());
		assertEquals("Resource Type", result.getType());
		assertEquals("Resource URL", result.getUrl());
		assertEquals(getSubtopic().getTitle(), result.getSubtopicName());
		assertEquals(getTopic().getTitle(), result.getTopicName());
	}

	@DisplayName("Throw NoSuchElementException if Resource does not exist")
	@Test
	public void readResourceResourceNotFound() {
		when(resourceDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> rs.readResource(1));
		verify(resourceDao, times(1)).findById(1);
	}

	@DisplayName("Update Resource if exists")
	@Test
	public void updateResource() {
		InResourceDto inResourceDto = new InResourceDto();
		inResourceDto.setTitle("New Title");
		Resource mockResource = getResource();
		Resource updatedResource = getResource();
		updatedResource.setTitle("New Title");

		when(resourceDao.findById(anyInt())).thenReturn(Optional.of(mockResource));
		when(resourceDao.save(any(Resource.class))).thenReturn(updatedResource);

		OutResourceDto result = rs.updateResource(1, inResourceDto);

		assertNotNull(result);
		assertEquals(1, result.getResourceId());
		assertEquals("New Title", result.getTitle());
		assertEquals("Resource Description", result.getDescription());
		assertEquals("Resource Type", result.getType());
		assertEquals("Resource URL", result.getUrl());
		assertEquals(getSubtopic().getTitle(), result.getSubtopicName());
		assertEquals(getTopic().getTitle(), result.getTopicName());
	}

	@DisplayName("Throw NoSuchElementException if Resource does not exist")
	@Test
	public void updateResourceResourceNotFound() {
		InResourceDto inResourceDto = new InResourceDto();
		inResourceDto.setTitle("New Title");

		when(resourceDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> rs.updateResource(1, inResourceDto));
		verify(resourceDao, times(1)).findById(1);
	}

}
