package com.demo;

import com.thebeastshop.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ForestScan(basePackages = "com.demo.forest")
public class HelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}
}
