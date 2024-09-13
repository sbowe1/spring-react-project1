package com.example.p1_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.security.auth.login.AccountNotFoundException;

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
import com.example.p1_backend.models.dtos.PlanContent;
import com.example.p1_backend.models.dtos.SubtopicWResources;
import com.example.p1_backend.models.dtos.TopicWResources;
import com.example.p1_backend.repositories.PlanDao;
import com.example.p1_backend.repositories.ResourceDao;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;
import com.example.p1_backend.repositories.UserDao;
import com.example.p1_backend.util.JwtUtil;

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

	String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0LXVzZXItdXNlcm5hbWUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvamVjdDF0ZWFtIiwiaWF0IjoxNzIwODE1NzE5LCJleHAiOjE3MjA5MDIxMTl9.sg_lpkxTLfCl-ucxM3VLKg112JhR2FV4dWptFQOqqks";

	/**
	 * Creates a mock user
	 * @return User
	 */
	private User getMockUser() {
		return new User("test-user-email@test.com", "test-user-password", "test-user-username", "ROLE_USER",
				"Spring Boot Roadmap");
	}

	@DisplayName("Create Plan")
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

		Plan plan = ps.createPlan("Bearer " + token, name);

		assertNotNull(plan);
		assertEquals("Spring Boot Roadmap", plan.getName());
	}

	@DisplayName("Throws KeyAlreadyExistsException when creating a plan that already exists")
	@Test
	public void createPlanAlreadyExists() {
		String name = "Spring Boot Roadmap";
		User mockUser = getMockUser();
		Plan mockPlan = new Plan(name, mockUser);

		when(planDao.getByName(name)).thenReturn(Optional.of(mockPlan));

		assertThrows(KeyAlreadyExistsException.class, () -> ps.createPlan("Bearer " + token, name));
		verify(planDao, times(1)).getByName(name);
	}

	@DisplayName("Throws AccountNotFoundException when creating a plan for a user that does not exist")
	@Test
	public void createPlanAccountNotFound() {
		String name = "Spring Boot Roadmap";
		User mockUser = getMockUser();
		mockUser.setUserId(1);

		when(planDao.getByName(name)).thenReturn(Optional.empty());
		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(userDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(AccountNotFoundException.class, () -> ps.createPlan("Bearer " + token, name));
		verify(planDao, times(1)).getByName(name);
		verify(jwtUtil, times(1)).extractUserId(token);
		verify(userDao, times(1)).findById(1);
	}

	@DisplayName("Return plan if exists")
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

	@DisplayName("Throw NoSuchElementException if Plan does not exist")
	@Test
	public void readPlanPlanNotFound() {
		when(planDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ps.readPlan(1));
		verify(planDao, times(1)).findById(anyInt());
	}

	@DisplayName("Return Plan if exists")
	@Test
	public void readPlanContents() {
		User mockUser = getMockUser();
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap", mockUser);
		List<Topic> mockTopicList = new ArrayList<>();
		Topic mockTopic = new Topic(1, "Topic 1", "Description", mockPlan, false);
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

		PlanContent result = ps.readPlanContents("Bearer " + token, 1);

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

	@DisplayName("Throw NoSuchElementException if Plan does not exist")
	@Test
	public void readPlanContentsPlanNotFound() {
		User mockUser = getMockUser();
		mockUser.setUserId(1);

		when(jwtUtil.extractUserId(anyString())).thenReturn(mockUser.getUserId());
		when(planDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ps.readPlanContents("Bearer " + token, 1));
		verify(jwtUtil, times(1)).extractUserId(token);
		verify(planDao, times(1)).findById(1);
	}

	@DisplayName("Delete plan")
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

	@DisplayName("Throw NoSuchElementException if Plan does not exist")
	@Test
	public void deletePlanPlanNotFound() {
		when(planDao.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> ps.deletePlan(1));
		verify(planDao, times(1)).findById(anyInt());
	}

	@DisplayName("Failed to delete plan")
	@Test
	public void deletePlanFailedToDelete() throws AccountNotFoundException {
		User mockUser = getMockUser();
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap", mockUser);

		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan)).thenReturn(Optional.of(mockPlan));

		String result = ps.deletePlan(1);

		assertEquals("Plan cannot be deleted", result);
		verify(planDao, times(2)).findById(1);
		verify(planDao).deleteById(1);
	}

}
