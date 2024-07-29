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
public class SubtopicWResources {

	private int subtopicId;

	private String title;

	private String description;

	private boolean status;

	@JsonIgnoreProperties({ "subtopic", "topic" })
	private List<Resource> resources;

	public SubtopicWResources(int subtopicId, String title, String description, boolean status) {
		this.subtopicId = subtopicId;
		this.title = title;
		this.description = description;
		this.status = status;
	}

}
