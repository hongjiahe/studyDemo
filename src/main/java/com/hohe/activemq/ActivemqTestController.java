package com.hohe.activemq;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.Destination;

@Controller

@RequestMapping("/activemqTest")
public class ActivemqTestController {

    /**
     * 1：创建一个队列 new ActiveMQQueue
     * 2: 注入消息提供者service
     * 3: 提供一个方法， 消息提供者发送消息
     */

    @Autowired
    private Producer producer;

    @RequestMapping("/test")
    public void test(){

        Destination destination  = new ActiveMQQueue("activemq_test_1");

        for(int i=1; i<= 50; i++){
            producer.sendMessage(destination, "广州恒大No1  "+i);
        }

    }

}
