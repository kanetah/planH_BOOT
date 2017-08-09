package com.kanetah.planH;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EntityScan("com.kanetah.planH.entity")
public class PlanHApplication {

	public static void main(String[] args) {

		SpringApplication.run(PlanHApplication.class, args);
	}
}
