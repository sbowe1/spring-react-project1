package com.example.p1_backend.models.dtos;

import com.example.p1_backend.models.Resource;
import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OutPlan {

	private int planId;

	private String name;

	private List<Topic> topics;

	private List<Subtopic> subtopics;

	private List<Resource> resources;

}
