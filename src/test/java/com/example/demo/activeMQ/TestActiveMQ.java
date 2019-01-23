package com.example.demo.activeMQ;

import com.example.demo.mq.IMessageProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * @Author: supengfei
 * @Date: 2019/1/23 10:19
 * @Description:
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestActiveMQ<StartSpringBootMain> {
    @Resource
    private IMessageProducerService messageProducerService;

    @Test
    public void testQueue(){
        for (int i = 0; i < 10; i++) {
            messageProducerService.sendQueueMessage("我发送的消息-Queue---"+i);
        }
    }
    @Test
    public void testTopic(){
        messageProducerService.sendTopicMessage("我发送的消息-Topic---");
        for (int i = 0; i < 10; i++) {
        }
    }
}
