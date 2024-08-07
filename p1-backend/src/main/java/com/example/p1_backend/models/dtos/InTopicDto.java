package com.example.p1_backend.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InTopicDto {

	private String topicName;

	private String description;

	public InTopicDto(String topicName, String description) {
		this.topicName = topicName;
		this.description = description;
	}

}
