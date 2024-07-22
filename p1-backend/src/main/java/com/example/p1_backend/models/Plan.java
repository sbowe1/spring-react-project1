package com.example.p1_backend.models;

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

	public Plan(int planId, String name) {
		this.planId = planId;
		this.name = name;
	}

	public Plan(String name) {
		this.name = name;
	}

}
