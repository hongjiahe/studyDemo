package com.hohe.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 *  activemq 消息消费者
 *
 */

@Component
public class Consumer3 {

    /**
     * 实现双向队列, 消费者消费完了之后, 返回指定的消息给消费提供者进行消费
     *
     * 1: 使用 @JmsListener配置消费者监听到的队列
     * 2: 提供一个消费消息的方法 (方法返回消息)
     * 3: 使用@SendTo("队列名")  相应的消息提供者需要对消息进行消费
     */


    @JmsListener(destination =  "activemq_test_1") //activemq_test_1 是需要监听的队列名
    @SendTo("out.queue")
    public String receiveQueue(String message){
        System.out.println(" [消费者3]: "+  message);

        StringBuffer text = new StringBuffer();
        text.append(" 我是广州吴彦祖 ");

        return text.toString();

    }

}
