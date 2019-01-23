package com.example.demo.mq.impl;

import com.example.demo.mq.IMessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.Topic;

/**
 * @Author: supengfei
 * @Date: 2019/1/23 10:16
 * @Description:
 */
@Service
public class MessageProducerService implements IMessageProducerService {

    @Autowired
    private Destination destination;
    @Autowired
    private Topic topic;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void sendQueueMessage(String msg) {
        jmsTemplate.convertAndSend(destination,msg);
    }

    @Override
    public void sendTopicMessage(String msg) {
        jmsTemplate.convertAndSend(topic,msg);
    }
}
