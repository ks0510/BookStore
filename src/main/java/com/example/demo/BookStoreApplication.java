package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;

@SpringBootApplication
public class BookStoreApplication {
	
	@Bean
	public ModelMapper getModelMapper() {
	return new ModelMapper();
	 }
	
	@Bean
	public SimpleMailMessage getSimpleMailMessage() {
		return new SimpleMailMessage();
	}


	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}

}
