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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {
    @Mock
    private PlanDao planDao;

    @InjectMocks
    private PlanService ps;

    // CREATE
    @Test
    public void createPlan(){
        String name = "Spring Boot Roadmap";
        Plan mockPlan = new Plan(name);
        mockPlan.setPlanId(1);

        when(planDao.getByName(name)).thenReturn(Optional.empty());
        when(planDao.save(any(Plan.class))).thenReturn(mockPlan);

        Plan plan = ps.createPlan(name);

        assertNotNull(plan);
        assertEquals("Spring Boot Roadmap", plan.getName());
    }
}
