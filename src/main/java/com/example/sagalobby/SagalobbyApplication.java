package com.example.sagalobby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SagalobbyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SagalobbyApplication.class, args);
	}

}
