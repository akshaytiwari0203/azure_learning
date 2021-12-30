package com.learning.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.learning")
public class HelloSpirngBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpirngBootApplication.class, args);
	}

}
