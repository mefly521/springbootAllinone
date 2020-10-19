package com.demo.controller;

import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
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
public class SendController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    //@Autowired
    //private Queue queue;
    @Autowired
    private Topic topic;

    /*
     * 发送 队列消息
     */
//    @RequestMapping("/sendQueue")
//    public String sendQueue() {
//        String message = UUID.randomUUID().toString();
//        // 指定消息发送的目的地及内容
//        this.jmsMessagingTemplate.convertAndSend(this.queue, message);
//        return "消息发送成功！message=" + message;
//    }

    //发送topic类型消息
    @GetMapping("/sendTopic")
    public void sendTopicMsg() {
        for (int i = 0; i < 1000; i++) {
            String msg = "topic:"+ i;
            jmsMessagingTemplate.convertAndSend(topic, msg);
        }
    }
}
