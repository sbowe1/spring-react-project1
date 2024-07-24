package com.example.p1_backend.models.dtos;

import com.example.p1_backend.models.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionNoUserDto {

	@JsonIgnoreProperties({ "user" })
	private Question question;

}
