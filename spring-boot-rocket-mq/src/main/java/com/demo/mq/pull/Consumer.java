package com.demo.mq.orderly;



import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;
import java.util.Random;

/**
 * 消费方式：这里，采用的是Consumer Push的方式，即设置Listener机制回调，相当于开启了一个线程。
 *
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

		consumer.registerMessageListener(new MessageListenerOrderly() {
			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				try {
					for (MessageExt messageExt:msgs) {

						System.out.println("order:"+new String(messageExt.getBody(),"utf-8"));
					}
					//模拟业务处理消息的时间
					//Thread.sleep(new Random().nextInt(10000000));
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(new Random().nextInt(10000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				return ConsumeOrderlyStatus.SUCCESS;
			}
		});


		consumer.start();
		System.out.println("Consumer Started.");
	}
}
