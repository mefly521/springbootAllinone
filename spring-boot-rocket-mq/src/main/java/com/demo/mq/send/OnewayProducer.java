package com.demo.mq.send;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 单向发送消息
 *
 * @author mifei
 * @create 2021-01-08 16:12
 **/
public class OnewayProducer {
	public static void main(String[] args) throws Exception{
		// 实例化消息生产者Producer
		DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
		// 设置NameServer的地址
		producer.setNamesrvAddr("localhost:9876");
		// 启动Producer实例
		producer.start();
		for (int i = 0; i < 100; i++) {
			// 创建消息，并指定Topic，Tag和消息体
			Message msg = new Message("TopicTest" /* Topic */,
					"TagA" /* Tag */,
					("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
			);
			// 发送单向消息，没有任何返回结果
			producer.sendOneway(msg);

		}
		// 如果不再发送消息，关闭Producer实例。
		producer.shutdown();
	}
}