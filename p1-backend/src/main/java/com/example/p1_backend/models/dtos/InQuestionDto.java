package com.example.p1_backend.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InQuestionDto {

	private String question;

	private String answer;

	private String topicName;

	public InQuestionDto(String question, String answer, String topicName) {
		this.question = question;
		this.answer = answer;
		this.topicName = topicName;
	}

}
