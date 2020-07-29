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

	/**
	 * 直接发送到队列
	 */
	public void send() {
		String context = "hello " + new Date();
		System.out.println("Sender : " + context);
		this.rabbitTemplate.convertAndSend("TestDirectQueue", context);
	}

	/**
	 * 发到交换机
	 * @return
	 */
	public String sendDirectMessage() {
		String messageId = String.valueOf(UUID.randomUUID());
		String messageData = "test message, hello!";
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String,Object> map=new HashMap<>();
		map.put("messageId",messageId);
		map.put("messageData",messageData);
		map.put("createTime",createTime);
		//将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
		rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
		return "ok";
	}


}