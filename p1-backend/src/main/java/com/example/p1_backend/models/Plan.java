package com.example.p1_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "plans")
@Component
@Data
@NoArgsConstructor
public class Plan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "planId")
	private int planId;

	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	@JsonIgnore
	private User user;

	public Plan(int planId, String name, User user) {
		this.planId = planId;
		this.name = name;
		this.user = user;
	}

	public Plan(String name, User user) {
		this.name = name;
		this.user = user;
	}

}
