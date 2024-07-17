package com.example.p1_backend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutResourceDto {

	private int resourceId;

	private String title;

	private String description;

	private String type;

	private String url;

	private String topicName;

	private String subtopicName;

}
