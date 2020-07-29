package com.demo.rabbit.sender;

import com.demo.rabbit.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class Sender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void sendTopicMessage1() {
		log.info("send message");
		rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_QUEUE_PER_QUEUE_TTL_NAME,"message 1111");
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_QUEUE_PER_QUEUE_TTL_NAME, "message 2222");
	}



}