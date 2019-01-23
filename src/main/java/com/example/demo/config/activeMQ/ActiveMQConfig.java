package com.example.demo.config.activeMQ;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;
import javax.jms.Topic;


/**
 * @Author: supengfei
 * @Date: 2019/1/23 10:06
 * @Description:
 */
@Configuration
public class ActiveMQConfig {

    @Bean("destination")
    public Destination getDestination(){
        Destination destination = new ActiveMQQueue("mytest.queue");
        return destination;
    }
    @Bean("topic")
    public Topic getTopic(){
        return new ActiveMQTopic("mytest.topic");
    }
}
