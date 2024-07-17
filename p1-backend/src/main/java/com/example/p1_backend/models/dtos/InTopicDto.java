package com.example.p1_backend.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InTopicDto {

	private String planName;

	private String topicName;

	public InTopicDto(String planName, String topicName) {
		this.planName = planName;
		this.topicName = topicName;
	}

}
