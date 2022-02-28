package com.marketlogicsoftware.pollingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class PollAppServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollAppServiceApplication.class, args);
	}

}
