package com.hohe.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

@Service("producer")

/**
 * 队列生产者
 */
public class Producer {

    /**
     *  1: 注入jmsTemplate  (在这里我使用的是 jmsMessagingTemplate (jmsMessagingTemplate 是对jmsTemplate的封装) )
     *  2: 提供一个发送消息的方法  sendMessage
     *
     */

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate ;

    //destination 是所需要发送到的队列, message是待发送的消息
    public void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

    @JmsListener(destination="out.queue")
    public void consumerMessage(String text){
        System.out.println("从out.queue队列收到的回复报文为:"+text);
    }

}
