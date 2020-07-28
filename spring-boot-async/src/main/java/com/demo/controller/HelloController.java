package com.demo.controller;

import com.demo.service.MyService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MyService.class);
    @Autowired
    private MyService myService;

    @RequestMapping("/test")
    public String getInedx() throws InterruptedException {
        logger.info("开始访问");
        long l1 = System.currentTimeMillis();
        myService.JobOne();
        myService.JobTwo();
        myService.JobThree();
        long l2 = System.currentTimeMillis();
        logger.info("结束访问,用时" + (l2 - l1));
        return "finished";
    }
}