package com.demo.controller;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * @author mifei
 * @create 2021-01-06 15:02
 **/
public class Producer {
	public static void main(String[] args) throws MQClientException, InterruptedException {
		DefaultMQProducer producer = new DefaultMQProducer("group_name");
		producer.setNamesrvAddr("192.168.2.222:9876;192.168.2.223:9876");
		producer.start();

		for (int i = 0; i < 100; i++) {
			try {
				Message msg = new Message("TopicTest",              // topic
						"TagA",                                     // tag
						("HelloWorld - RocketMQ" + i).getBytes()    // body
				);
				SendResult sendResult = producer.send(msg);
				System.out.println(sendResult);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep(1000);
			}
		}

		producer.shutdown();
	}
}
