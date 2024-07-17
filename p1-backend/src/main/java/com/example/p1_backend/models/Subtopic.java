package com.example.p1_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "subtopics")
@Component
@Data
@NoArgsConstructor
public class Subtopic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subtopicId")
	private int subtopicId;

	private String title;

	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "topicId")
	@JsonIgnoreProperties({ "planId", "status" })
	private Topic topic;

	private boolean status;

	public Subtopic(int subtopicId, String title, String description, Topic topic, boolean status) {
		this.subtopicId = subtopicId;
		this.title = title;
		this.description = description;
		this.topic = topic;
		this.status = status;
	}

	public Subtopic(String title, String description, Topic topic, boolean status) {
		this.title = title;
		this.description = description;
		this.topic = topic;
		this.status = status;
	}

}
