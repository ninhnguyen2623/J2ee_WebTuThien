package com.example.springdonateweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.example.springdonateweb", "com.example.springdonateweb.Services.mappers"})
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SpringDonateWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDonateWebApplication.class, args);
	}

}
