package com.demo.rabbit.sender;

import com.demo.rabbit.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
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
		//rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME,(Object) "message 2222",new ExpirationMessagePostProcessor(100000+""));
		//rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME,(Object) "message 1111",new ExpirationMessagePostProcessor(1000+""));

		this.rabbitTemplate.convertAndSend(DelayQueueConfig.MQ_DELAY_EXCHANGE,
				DelayQueueConfig.MQ_DELAY, "send message 2222", (Message message)->{
			message.getMessageProperties().setDelay(5000);
			return message;
		} );
		this.rabbitTemplate.convertAndSend(DelayQueueConfig.MQ_DELAY_EXCHANGE,
				DelayQueueConfig.MQ_DELAY, "send message 111", (Message message)->{
					message.getMessageProperties().setDelay(1000);
					return message;
				} );
	}



}