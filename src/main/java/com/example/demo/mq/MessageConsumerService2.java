package com.example.demo.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author: supengfei
 * @Date: 2019/1/23 10:39
 * @Description:
 */
@Component
public class MessageConsumerService2 {

    @JmsListener(destination = "mytest.queue")
    public void receiveQueueMessage(String text){
        System.out.println("接受到的消息为2-----"+text);
    }
    @JmsListener(destination = "mytest.topic")
    public void receiveTopicMessage(String text){
        System.out.println("接受到的消息为2-----"+text);
    }
}
