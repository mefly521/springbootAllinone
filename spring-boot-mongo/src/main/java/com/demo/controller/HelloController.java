package com.demo.controller;

import com.demo.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
    @RequestMapping("/")
    public String index() {
        return "Hello Spring Boot 2.0!";
    }
    @RequestMapping("/test")
    public Object test() {
        User user = new User();
        user.setUserName("mefly");
        return user;
    }
}