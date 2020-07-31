package com.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
    @RequestMapping("/")
    public String index() {
        return "Hello Spring Boot 2.0!";
    }
    @RequestMapping("/test")
    public Object test() throws InterruptedException {
        Thread.sleep(30_000);
        User user = new User();
        user.setUsername("mefly");
        return user;
    }
}