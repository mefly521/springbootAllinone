package com.demo.mq.push;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 消费方式：这里，采用的是Consumer Push的方式，即设置Listener机制回调，相当于开启了一个线程。
 * @author mifei
 * @create 2021-01-06 15:06
 **/
public class Consumer {
	public static void main(String[] args) throws InterruptedException, MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group_name");
		consumer.setNamesrvAddr("dev.rocketmq1:9876");
		/**
		 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
		 * 如果非第一次启动，那么按照上次消费的位置继续消费
		 */
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("testTopic_mifei", "*");
		consumer.setConsumeMessageBatchMaxSize(10);

		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println(msgs.size());
				for (MessageExt msg: msgs) {
					try {
						String topic = msg.getTopic();
						String tags = msg.getTags();
						String msgBody = new String(msg.getBody(),"utf-8");
						System.out.println("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" +msgBody);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		consumer.start();
		System.out.println("Consumer Started.");
	}
}
