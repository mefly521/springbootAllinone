package com.demo.mq.orderly;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * @author mifei
 * @create 2021-01-06 15:02
 **/
public class Producer {
	public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
		DefaultMQProducer producer = new DefaultMQProducer("OrderProducer");
		producer.setNamesrvAddr("dev.rocketmq1:9876");
		producer.start();

		String[] tags = new String[]{"createTag", "payTag", "sendTag"};

		for (int orderId = 1; orderId <= 1; orderId++) {	//订单消息
			for (int type = 0; type < 100; type++) {			//每种订单分为：创建订单/支付订单/发货订单
				Message msg = new Message("testTopic_mifei",
						tags[type % tags.length],
						orderId + ":" + type,
						(orderId + ":" + type).getBytes()
				);
				SendResult sendResult = producer.send(msg, new MessageQueueSelector(){
					@Override
					public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg){
						Integer id = (Integer) arg;
						int index = id % mqs.size();
						return mqs.get(index);
					}
				}, orderId);
				System.out.println(sendResult);
			}
		}

	}
}
