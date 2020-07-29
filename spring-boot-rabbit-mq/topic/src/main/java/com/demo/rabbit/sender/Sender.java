package com.demo.rabbit.sender;

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
public class Sender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public String sendTopicMessage1() {
		String messageId = String.valueOf(UUID.randomUUID());
		String messageData = "message: M A N ";
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String, Object> manMap = new HashMap<>();
		manMap.put("messageId", messageId);
		manMap.put("messageData", messageData);
		manMap.put("createTime", createTime);
		rabbitTemplate.convertAndSend("TestTopicExchange", "topic.man", manMap);
		return "ok";
	}

	public String sendTopicMessage2() {
		String messageId = String.valueOf(UUID.randomUUID());
		String messageData = "message: woman is all ";
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String, Object> womanMap = new HashMap<>();
		womanMap.put("messageId", messageId);
		womanMap.put("messageData", messageData);
		womanMap.put("createTime", createTime);
		rabbitTemplate.convertAndSend("TestTopicExchange", "topic.woman", womanMap);
		return "ok";
	}

}