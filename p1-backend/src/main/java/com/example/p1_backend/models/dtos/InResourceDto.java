package com.example.p1_backend.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InResourceDto {

	private String title;

	private String description;

	private String type;

	private String url;

	public InResourceDto(String title, String description, String type, String url) {
		this.title = title;
		this.description = description;
		this.type = type;
		this.url = url;
	}

	public InResourceDto(String title, String description, String type) {
		this.title = title;
		this.description = description;
		this.type = type;
	}

}
