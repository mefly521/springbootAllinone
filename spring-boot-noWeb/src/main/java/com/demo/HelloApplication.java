package com.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Date;

@SpringBootApplication
public class HelloApplication implements ApplicationRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(HelloApplication.class)
				.web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
				.bannerMode(Banner.Mode.CONSOLE)
				.run(args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		while (true) {
			System.out.println("now is " + new Date().toLocaleString());
			Thread.sleep(1000);
		}
	}

}
