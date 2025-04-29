package com.userauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UserAuthApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserAuthApiApplication.class, args);
	}
}