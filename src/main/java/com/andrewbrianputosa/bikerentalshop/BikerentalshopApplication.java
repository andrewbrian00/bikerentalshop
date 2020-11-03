package com.andrewbrianputosa.bikerentalshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({"com.andrewbrianputosa.bikerentalshop"})
public class BikerentalshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikerentalshopApplication.class, args);
	}

}
