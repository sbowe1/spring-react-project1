package com.example.p1_backend.models.dtos;

import com.example.p1_backend.models.Plan;
import lombok.Data;

import java.util.List;

@Data
public class OutUserDto {

	private String username;

	private List<String> roles;

	private List<String> plans;

	private String token;

	public OutUserDto(String token, List<String> plans, List<String> roles, String username) {
		this.token = token;
		this.plans = plans;
		this.roles = roles;
		this.username = username;
	}

}
