package com.example.p1_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Component
@Data
@NoArgsConstructor
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "questionId")
	private int questionId;

	private String question;

	private String answer;

	private boolean correct;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "topicId")
	@JsonIgnoreProperties({ "planId", "status" })
	private Topic topic;

	public Question(int questionId, String question, String answer, boolean correct, Topic topic) {
		this.questionId = questionId;
		this.question = question;
		this.answer = answer;
		this.correct = correct;
		this.topic = topic;
	}

}
