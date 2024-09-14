package com.example.p1_backend.models.dtos;

import lombok.Data;

@Data
public class LoginDto {

	private String email;

	private String password;

	public LoginDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

}
