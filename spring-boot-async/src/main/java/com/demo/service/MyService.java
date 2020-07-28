package com.demo.service;

import com.fasterxml.jackson.core.filter.TokenFilter;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author mifei
 * @create 2020-01-06 9:48
 **/
@Service
public class MyService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MyService.class);
    @Async
    public void JobOne() throws InterruptedException {
        logger.info("开始执行任务一");
        long l1 = System.currentTimeMillis();
        Thread.sleep(2000);
        long l2 = System.currentTimeMillis();
        logger.info("任务一用时"+(l2-l1));

    }

    @Async
    public void JobTwo() throws InterruptedException {
        logger.info("开始执行任务二");
        long l1 = System.currentTimeMillis();
        Thread.sleep(2000);
        long l2 = System.currentTimeMillis();
        logger.info("任务二用时"+(l2-l1));
    }


    @Async
    public void JobThree() throws InterruptedException {
        logger.info("开始执行任务三");
        long l1 = System.currentTimeMillis();
        Thread.sleep(2000);
        long l2 = System.currentTimeMillis();
        logger.info("任务三用时"+(l2-l1));
    }
}
