package com.demo.mq;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author mifei
 * @create 2021-01-06 15:02
 **/
public class Producer {
	public static void main(String[] args) throws MQClientException, InterruptedException {
		DefaultMQProducer producer = new DefaultMQProducer("group_name");
		producer.setNamesrvAddr("dev.rocketmq1:9876");
		producer.start();

		for (int i = 0; i < 100; i++) {
			try {
				Message msg = new Message("testTopic_mifei",              // topic
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
