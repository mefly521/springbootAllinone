package com.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;
import java.util.UUID;

/**
 * @author mifei
 * @create 2020-07-30 9:29
 **/
@RestController
@Slf4j
public class QueueCustomerController {
//    /*
//     * 监听和接收  队列消息
//     */
//    @JmsListener(destination="springboot.queue")
//    public void readActiveQueue(String message) {
//        System.out.println("接受到：" + message);
//    }

    //接收topic类型消息
    //destination对应配置类中ActiveMQTopic("springboot.topic")设置的名字
    //containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
    @JmsListener(destination="mq.alarm.msg.topic.1", containerFactory = "jmsTopicListenerContainerFactory")
    public void ListenTopic(String msg) throws InterruptedException {
        Thread.sleep(2000L);
        log.info("接收到topic消息：" + msg);
    }
}
