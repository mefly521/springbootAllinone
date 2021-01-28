package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloApplication {
	private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);

		System.out.println( "Hello World!" );

		System.out.println("----> logback start");
		logger.trace("--> Hello trace.");
		logger.debug("--> Hello debug.");
		logger.info("--> Hello info.");
		logger.warn("--> Goodbye warn.");
		logger.error("--> Goodbye error.");
		System.out.println("----> logback end");
	}
}
