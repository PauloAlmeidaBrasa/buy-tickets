package com.example.buy_tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.buy_tickets.config.DotenvPropertySourceFactory;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = "file:${user.dir}/.env", ignoreResourceNotFound = true, factory = DotenvPropertySourceFactory.class)
public class BuyTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyTicketsApplication.class, args);
	}

}
