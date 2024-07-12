package com.example.p1_backend.models.dtos;

import lombok.Data;

@Data
public class RegisterDto {

	private String email;

	private String password;

	private String username;

	public RegisterDto(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
	}

}
