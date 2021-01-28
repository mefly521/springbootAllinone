package com.demo.mq.retry;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 只重试3次
 * @author mifei
 * @create 2021-01-08 15:19
 **/
public class Consumer {
	public static void main(String[] args) throws InterruptedException, MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group_name");
		consumer.setNamesrvAddr("192.168.2.222:9876;192.168.2.223:9876");
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("TopicTest", "*");

		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				try {
					MessageExt msg = msgs.get(0);
					String msgbody = new String(msg.getBody(), "utf-8");
					System.out.println(msgbody + " Receive New Messages: " + msgs);
					if (msgbody.equals("HelloWorld - RocketMQ4")) {
						System.out.println("======错误=======");
						int a = 1 / 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (msgs.get(0).getReconsumeTimes() == 3) {  //只重试3次
						// 该条消息可以存储到DB或者LOG日志中，或其他处理方式
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;// 成功
					} else {
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;// 重试
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		consumer.start();
		System.out.println("Consumer Started.");
	}
}
