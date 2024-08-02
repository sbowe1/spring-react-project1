package com.example.p1_backend.services;

import com.example.p1_backend.models.*;
import com.example.p1_backend.models.dtos.PlanContent;
import com.example.p1_backend.models.dtos.SubtopicWResources;
import com.example.p1_backend.models.dtos.TopicWResources;
import com.example.p1_backend.repositories.*;
import com.example.p1_backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private UserDao userDao;

	@Mock
	private PlanDao planDao;

	@Mock
	private TopicDao topicDao;

	@Mock
	private SubtopicDao subtopicDao;

	@Mock
	ResourceDao resourceDao;

	@InjectMocks
	private PlanService ps;

	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	private String getToken() {
		return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";
	}

	// CREATE
	@Test
	public void createPlan() throws AccountNotFoundException {
		String name = "Spring Boot Roadmap";
		User mockUser = getMockUser();
		mockUser.setPlans(new ArrayList<>());
		User mockUserAfter = getMockUser();
		Plan mockPlan = new Plan(name, mockUser);
		mockPlan.setPlanId(1);

		when(planDao.getByName(name)).thenReturn(Optional.empty());
		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(userDao.findById(anyInt())).thenReturn(Optional.of(mockUser));
		when(planDao.save(any(Plan.class))).thenReturn(mockPlan);
		when(userDao.save(any(User.class))).thenReturn(mockUserAfter);

		Plan plan = ps.createPlan("Bearer " + getToken(), name);

		assertNotNull(plan);
		assertEquals("Spring Boot Roadmap", plan.getName());
	}

	// READ
	@Test
	public void readPlan() {
		User mockUser = getMockUser();
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap", mockUser);

		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan));

		Plan result = ps.readPlan(1);

		assertNotNull(result);
		assertEquals(1, result.getPlanId());
		assertEquals("Spring Boot Roadmap", result.getName());
		assertEquals(mockUser, result.getUser());
		verify(planDao, times(1)).findById(anyInt());
	}

	// READ CONTENTS
	@Test
	public void readPlanContents() {
		User mockUser = getMockUser();
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap", mockUser);
		List<Topic> mockTopicList = new ArrayList<>();
		Topic mockTopic = new Topic(1, "Topic 1", mockPlan, false);
		mockTopicList.add(mockTopic);
		List<Subtopic> mockSubtopicList = new ArrayList<>();
		Subtopic mockSubtopic = new Subtopic(1, "Subtopic 1", "Description", mockTopic, false);
		mockSubtopicList.add(mockSubtopic);
		List<Resource> mockResourceList = new ArrayList<>();
		mockResourceList.add(new Resource(1, "Resource 1", "Description", "Type", "URL", null, mockTopic));
		mockResourceList.add(new Resource(2, "Resource 2", "Description", "Type", "URL", mockSubtopic, mockTopic));

		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan));
		when(topicDao.findAllByPlanPlanId(anyInt())).thenReturn(mockTopicList);
		when(resourceDao.findAllByTopicTopicId(anyInt())).thenReturn(mockResourceList);
		when(subtopicDao.findAllByTopicTopicId(anyInt())).thenReturn(mockSubtopicList);

		PlanContent result = ps.readPlanContents("Bearer " + getToken(), 1);

		assertNotNull(result);
		assertEquals(1, result.getPlanId());
		assertEquals("Spring Boot Roadmap", result.getPlanName());
		assertEquals(new TopicWResources(mockTopic.getTopicId(), mockTopic.getTitle(), mockTopic.isStatus(),
				List.of(mockResourceList.get(0))), result.getContent().get(0));
		assertEquals(
				new SubtopicWResources(mockSubtopic.getSubtopicId(), mockSubtopic.getTitle(),
						mockSubtopic.getDescription(), mockSubtopic.isStatus(), List.of(mockResourceList.get(1))),
				result.getContent().get(1));
	}

	// DELETE
	@Test
	public void deletePlan() throws AccountNotFoundException {
		User mockUser = getMockUser();
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap", mockUser);

		// Returns mockPlan the first time, and an empty Optional the second time
		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan)).thenReturn(Optional.empty());
		doNothing().when(planDao).deleteById(1);
		when(userDao.findById(anyInt())).thenReturn(Optional.of(mockUser));
		mockUser.setPlans(new ArrayList<>());
		when(userDao.save(any(User.class))).thenReturn(mockUser);

		String message = ps.deletePlan(1);

		assertEquals("Plan successfully deleted", message);
		verify(planDao, atMost(1)).deleteById(1);
		verify(planDao, atMost(2)).findById(1);
	}

}
