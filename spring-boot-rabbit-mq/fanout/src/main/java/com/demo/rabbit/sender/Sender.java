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


	public String sendFanoutMessage() {
		System.out.println("sendFanoutMessage");
		String messageId = String.valueOf(UUID.randomUUID());
		String messageData = "message: testFanoutMessage ";
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String, Object> map = new HashMap<>();
		map.put("messageId", messageId);
		map.put("messageData", messageData);
		map.put("createTime", createTime);
		rabbitTemplate.convertAndSend("TestFanoutExchange", null, map);
		return "ok";
	}

}