package com.example.p1_backend.models.dtos;

import com.example.p1_backend.models.Resource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicWResources {

	private int topicId;

	private String title;

	private boolean status;

	@JsonIgnoreProperties({ "subtopic", "topic" })
	private List<Resource> resources;

	public TopicWResources(int topicId, String title, boolean status) {
		this.topicId = topicId;
		this.title = title;
		this.status = status;
	}

}
