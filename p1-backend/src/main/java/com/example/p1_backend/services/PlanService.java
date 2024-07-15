package com.example.p1_backend.services;

import com.example.p1_backend.models.Plan;
import com.example.p1_backend.repositories.PlanDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Optional;

@Service
@Slf4j
public class PlanService {
    private final PlanDao planDao;

    @Autowired
    public PlanService(PlanDao planDao) {
        this.planDao = planDao;
    }

    // CREATE
    public Plan createPlan(String name){
        Optional<Plan> optPlan = planDao.getByName(name);
        if(optPlan.isPresent()){
            throw new KeyAlreadyExistsException("That plan already exists");
        }

        Plan newPlan = planDao.save(new Plan(name));
        return newPlan;
    }

    //
}
