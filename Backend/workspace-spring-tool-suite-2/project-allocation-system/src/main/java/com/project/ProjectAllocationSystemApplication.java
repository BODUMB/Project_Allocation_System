package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProjectAllocationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectAllocationSystemApplication.class, args);
	}

}

