package com.demo.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mefly
 */
@Configuration
public class DelayQueueConfig {

    public static final String MQ_DELAY = "test.topic.delay";
    public static final String MQ_DELAY_EXCHANGE = "test.topicExchangeToDelay";


    @Bean
    CustomExchange exchangeToDelay() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MQ_DELAY_EXCHANGE,"x-delayed-message",true,false,args);
    }

    @Bean
    Binding bindingExchangeMessageToDelay() {
        return BindingBuilder.bind(queueMessageDelay()).to(exchangeToDelay()).with(MQ_DELAY).noargs();
    }

    @Bean
    public Queue queueMessageDelay(){
        return new Queue(MQ_DELAY);
    }
}
