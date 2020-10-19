package com.demo.config;

import com.demo.mq.CustomActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

@Configuration
public class BeanConfig {

    @Value("${spring.activemq.user}")
    private String usrName;

    @Value("${spring.activemq.password}")
    private  String password;

    @Value("${spring.activemq.broker-url}")
    private  String brokerUrl;

//    @Bean
//    public Queue queue() {
//        return new ActiveMQQueue("springboot.queue");
//    }

//    @Bean("connectionFactory")
//    public ActiveMQConnectionFactory connectionFactory() throws Exception {
////        ActiveMQSslConnectionFactory factory = new ActiveMQSslConnectionFactory();
////        factory.setUserName(usrName);
////        factory.setPassword(password);
////        factory.setBrokerURL(brokerUrl);
////        factory.setTrustStore("tomcat.keystore");
////        factory.setUseAsyncSend(true);
//        CustomActiveMQConnectionFactory factory = new CustomActiveMQConnectionFactory();
//        factory.setBrokerURL(brokerUrl);
//        factory.setUserName(usrName);
//        factory.setPassword(password);
//        factory.setTrustStore("tomcat.keystore");
//        factory.setUseAsyncSend(true);
//        return factory;
//    }

    //springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置该bean@Qualifier("connectionFactory")
    @Bean
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public Topic topic() {
        return new ActiveMQTopic("mq.alarm.msg.topic.1") ;
    }
}