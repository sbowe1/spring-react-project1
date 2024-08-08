package com.example.p1_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "planId")
	@JsonIgnore
	private Plan plan;

	private boolean status;

	public Topic(int topicId, String title, String description, Plan plan, boolean status) {
		this.topicId = topicId;
		this.title = title;
		this.description = description;
		this.plan = plan;
		this.status = status;
	}

	public Topic(String title, String description, Plan plan, boolean status) {
		this.title = title;
		this.description = description;
		this.plan = plan;
		this.status = status;
	}

}
