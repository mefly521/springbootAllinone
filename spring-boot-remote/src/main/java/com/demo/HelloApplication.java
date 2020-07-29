package com.demo;

import com.demo.controller.CommandUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class HelloApplication {

	public static void main(String[] args) {
		//SpringApplication.run(HelloApplication.class, args);
		try {
			String res = CommandUtil.run("ll");
			System.out.println(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
