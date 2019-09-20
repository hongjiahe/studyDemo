package com.hohe.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *  activemq 消息消费者
 *
 */

@Component
public class Consumer2 {

    /**
     * 1: 使用 @JmsListener配置消费者监听到的队列
     * 2: 提供一个消费消息的方法
     */


    @JmsListener(destination =  "activemq_test_1") //activemq_test_1 是需要监听的队列名
    public void receiveQueue(String message){
        System.out.println(" [消费者2]: "+  message);
    }

    //消费者2的代码同上，注意，消息消费者的类上必须加上@Component，或者是@Service，这样的话，消息消费者类就会被委派给Listener类，原理类似于使用SessionAwareMessageListener以及MessageListenerAdapter来实现消息驱动POJO


}
