package com.example.p1_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "resources")
@Component
@Data
@NoArgsConstructor
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "resourceId")
	private int resourceId;

	private String title;

	private String description;

	@Column(nullable = false)
	private String type;

	private String url;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subtopicId")
	@JsonIgnoreProperties({ "description", "topicId", "status" })
	private Subtopic subtopic;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "topicId", nullable = false)
	@JsonIgnoreProperties({ "planId", "status" })
	private Topic topic;

	public Resource(int resourceId, String title, String description, String type, String url, Subtopic subtopic,
			Topic topic) {
		this.resourceId = resourceId;
		this.title = title;
		this.description = description;
		this.type = type;
		this.url = url;
		this.subtopic = subtopic;
		this.topic = topic;
	}

}
