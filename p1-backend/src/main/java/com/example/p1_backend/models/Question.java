package com.example.p1_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "questions")
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
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "topicId")
	@JsonIgnoreProperties({ "planId", "status" })
	private Topic topic;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "userId")
	@JsonIgnore
	private User user;

	public Question(int questionId, String question, String answer, boolean correct, Topic topic) {
		this.questionId = questionId;
		this.question = question;
		this.answer = answer;
		this.correct = correct;
		this.topic = topic;
	}

	public Question(String question, String answer, boolean correct, Topic topic, User user) {
		this.question = question;
		this.answer = answer;
		this.correct = correct;
		this.topic = topic;
		this.user = user;
	}

}
