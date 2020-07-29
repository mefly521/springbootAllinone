package com.demo.rabbit.receiver;

import com.demo.rabbit.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mifei
 * @create 2020-04-16 14:57
 **/
@Component
@Slf4j
public class DelayReceiver {

    @RabbitListener(queues = DelayQueueConfig.DELAY_PROCESS_QUEUE_NAME)
    @RabbitHandler
    public void process(String testMessage) {
        log.info("DelayReceiver 消费者收到消息  : " + testMessage);
    }
}