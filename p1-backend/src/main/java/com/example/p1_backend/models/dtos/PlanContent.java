package com.example.p1_backend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanContent {

	private int planId;

	private String planName;

	private List<Object> content;

}
