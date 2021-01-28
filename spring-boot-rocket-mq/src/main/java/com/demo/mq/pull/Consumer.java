package com.demo.mq.pull;


import org.apache.rocketmq.client.consumer.*;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Date;
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
		MQPullConsumerScheduleService mqPullConsumerScheduleService = new MQPullConsumerScheduleService("group_name");
		mqPullConsumerScheduleService.getDefaultMQPullConsumer().setNamesrvAddr("dev.rocketmq1:9876");
		mqPullConsumerScheduleService.setMessageModel(MessageModel.CLUSTERING);

		mqPullConsumerScheduleService.registerPullTaskCallback("testTopic_mifei", new PullTaskCallback() {
			@Override
			public void doPullTask(MessageQueue messageQueue, PullTaskContext pullTaskContext) {
				System.out.println(new Date() + "===拉取数据开始");
				MQPullConsumer mqPullConsumer = pullTaskContext.getPullConsumer();
				try {
					long offset = mqPullConsumer.fetchConsumeOffset(messageQueue, false);
					if (offset < 0) {
						offset = 0;
					}
					PullResult pullResult = mqPullConsumer.pull(messageQueue, "*", offset, 32);
					System.out.println("pull status:" + pullResult.getPullStatus());
					switch (pullResult.getPullStatus()) {
						case FOUND:
							List<MessageExt> msgFoundList = pullResult.getMsgFoundList();
							for (MessageExt messageExt : msgFoundList) {
								//业务开始消费数据
								try {
									System.out.println("pull msg:" + new String(messageExt.getBody()));
								} catch (Exception e) {
									//TODo:异常处理
								}
							}
							break;
						case NO_MATCHED_MSG:
						case NO_NEW_MSG:
						case OFFSET_ILLEGAL:
						default:
							break;
					}
					//更新offset 5S以及设置下次拉取时间
					mqPullConsumer.updateConsumeOffset(messageQueue, pullResult.getNextBeginOffset());
					pullTaskContext.setPullNextDelayTimeMillis(10 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(new Date() + "==========拉取数据结束");
			}
		});
		mqPullConsumerScheduleService.start();
	}
}
