package com.hohe.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

    private static final int SEND_NUMBER = 2000;

    public static void main(String[] args) {
        // ConnectionFactory ：连接工厂，JMS用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS客户端到JMS Provider的连接
        Connection connection = null;
        // Session：一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;
        // TextMessage message;
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 获取session，FirstQueue是一个服务器的queue
            destination = session.createQueue("FirstQueue");
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息
            sendMessage(session, producer);
            // session.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer) throws Exception {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("ActiveMQ发送消息" + i);
            System.out.println("发送消息：ActiveMQ发送的消息" + i);
            producer.send(message);
        }
    }

}
