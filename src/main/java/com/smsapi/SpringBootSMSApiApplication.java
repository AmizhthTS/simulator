package com.smsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SpringBootSMSApiApplication {

	
	
	public static void main(String[] args) {
		
		
		System.setProperty("spring.main.allow-bean-definition-overriding","true");
	    System.setProperty("server.servlet.context-path", "/sms-api");

		System.setProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults","false");
		System.setProperty("spring.jpa.open-in-view","false");
		System.setProperty("smsapi.username","root");
		System.setProperty("smsapi.password","root123");
		
		System.setProperty("spring.main.lazy-initialization", "false");
	
		SpringApplication.run(SpringBootSMSApiApplication.class, args);
		
	}
	
	
	
	
}