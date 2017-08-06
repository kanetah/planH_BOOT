package com.kanetah.planH;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("planH.entity")
public class ApplicationLaunch {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationLaunch.class, args);
	}
}
