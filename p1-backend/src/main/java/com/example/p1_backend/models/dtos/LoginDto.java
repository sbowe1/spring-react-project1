package com.example.p1_backend.models.dtos;

import lombok.Data;

@Data
public class LoginDto {

	private String username;

	private String password;

	public LoginDto(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
