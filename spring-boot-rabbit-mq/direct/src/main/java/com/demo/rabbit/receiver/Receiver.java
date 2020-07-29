package com.demo.rabbit.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@RabbitListener(queues = "TestDirectQueue")
public class Receiver {

    @RabbitHandler
    public void process(Object hello) {
        System.out.println("Receiver : " + hello);
    }

}
