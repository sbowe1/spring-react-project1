package com.example.p1_backend.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InSubtopicDto {

	private String title;

	private String description;

	public InSubtopicDto(String title, String description) {
		this.title = title;
		this.description = description;
	}

}
