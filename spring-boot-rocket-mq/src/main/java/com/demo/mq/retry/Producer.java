package com.demo.mq.retry;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * Producer端重试
 * 如果该条消息在1S内没有发送成功，那么重试3次。
 * @author mifei
 * @create 2021-01-06 15:02
 **/
public class Producer {
	public static void main(String[] args) throws MQClientException, InterruptedException {
		DefaultMQProducer producer = new DefaultMQProducer("group_name");
		producer.setNamesrvAddr("192.168.2.222:9876;192.168.2.223:9876");
		producer.setRetryTimesWhenSendFailed(3);////失败的情况重发3次
		producer.start();

		for (int i = 0; i < 100; i++) {
			try {
				Message msg = new Message("TopicTest", 				// topic
						"TagA", 									// tag
						("HelloWorld - RocketMQ" + i).getBytes()	// body
				);
				SendResult sendResult = producer.send(msg, 1000);////消息在1S内没有发送成功，就会重试
				System.out.println(sendResult);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep(1000);
			}
		}

		producer.shutdown();
	}
}
