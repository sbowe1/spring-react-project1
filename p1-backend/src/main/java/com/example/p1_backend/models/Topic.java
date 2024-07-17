package com.example.p1_backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "topics")
@Component
@Data
@NoArgsConstructor
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "topicId")
	private int topicId;

	private String title;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "planId")
	private Plan plan;

	private boolean status;

	public Topic(int topicId, String title, Plan plan, boolean status) {
		this.topicId = topicId;
		this.title = title;
		this.plan = plan;
		this.status = status;
	}

	public Topic(String title, Plan plan, boolean status) {
		this.title = title;
		this.plan = plan;
		this.status = status;
	}

}
