package com.example.demo.mq;

/**
 * @Author: supengfei
 * @Date: 2019/1/23 10:14
 * @Description:
 */
public interface IMessageProducerService {
    /**
     * @Description //TODO 点对点
     * @Param 
     * @return 
     **/
    void sendQueueMessage(String msg);
    /**
     * @Description //TODO 订阅 pub sub
     * @Param 
     * @return 
     **/
    void sendTopicMessage(String msg);
}
