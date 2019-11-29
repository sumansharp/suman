package com.efficenz;

/**
 * <h1>Kickstarter class for Efficenz Services</h1>
 * @author Efficenz
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//import org.springframework.http.HttpStatus;
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan({"controller","service"})
public class EfficenzwebioApplication {

	 
	 
	public static void main(String[] args) {
		SpringApplication.run(EfficenzwebioApplication.class, args);
	}
	
		 
}
