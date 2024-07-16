package com.example.p1_backend.services;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.repositories.PlanDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

	@Mock
	private PlanDao planDao;

	@InjectMocks
	private PlanService ps;

	// CREATE
	@Test
	public void createPlan() {
		String name = "Spring Boot Roadmap";
		Plan mockPlan = new Plan(name);
		mockPlan.setPlanId(1);

		when(planDao.getByName(name)).thenReturn(Optional.empty());
		when(planDao.save(any(Plan.class))).thenReturn(mockPlan);

		Plan plan = ps.createPlan(name);

		assertNotNull(plan);
		assertEquals("Spring Boot Roadmap", plan.getName());
	}

	// READ
	@Test
	public void readPlan() {
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap");

		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan));

		Plan result = ps.readPlan(1);

		assertNotNull(result);
		assertEquals(1, result.getPlanId());
		assertEquals("Spring Boot Roadmap", result.getName());
		verify(planDao, times(1)).findById(anyInt());
	}

	// DELETE
	@Test
	public void deletePlan() {
		Plan mockPlan = new Plan(1, "Spring Boot Roadmap");

		// Returns mockPlan the first time, and an empty Optional the second time
		when(planDao.findById(anyInt())).thenReturn(Optional.of(mockPlan)).thenReturn(Optional.empty());
		doNothing().when(planDao).deleteById(1);

		String message = ps.deletePlan(1);

		assertEquals("Plan successfully deleted", message);
		verify(planDao, atMost(1)).deleteById(1);
		verify(planDao, atMost(2)).findById(1);
	}

}
