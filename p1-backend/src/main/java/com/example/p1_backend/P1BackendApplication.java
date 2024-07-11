package com.example.p1_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.example.p1_backend")
@EntityScan("com.example.p1_backend.models")
@EnableJpaRepositories("com.example.p1_backend.repositories")
public class P1BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(P1BackendApplication.class, args);
	}

}
