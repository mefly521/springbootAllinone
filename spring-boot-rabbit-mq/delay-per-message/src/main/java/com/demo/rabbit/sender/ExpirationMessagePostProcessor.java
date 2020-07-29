package com.demo.rabbit.sender;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * @author mifei
 * @create 2020-04-17 16:02
 **/
public class ExpirationMessagePostProcessor implements MessagePostProcessor {
    private String ttl;

    public ExpirationMessagePostProcessor(String ttl) {
        this.ttl = ttl;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setExpiration(ttl);
        return message;
    }
}
