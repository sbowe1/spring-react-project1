package com.example.p1_backend.models.dtos;

import lombok.Data;

@Data
public class RegisterDto {

	private String email;

	private String password;

	private String name;

	public RegisterDto(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}

}
