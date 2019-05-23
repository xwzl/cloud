package com.cloud.order.message.test;

import org.apache.tomcat.jni.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender1 {

    @Autowired
    private AmqpTemplate rabbitTemplate;


    /**
     * 单生产者和单消费者
     */
    public void send() {
        String sendMsg = "hello1 " + new Date();
        System.out.println("Sender1 : " + sendMsg);
        this.rabbitTemplate.convertAndSend("hello", sendMsg);
    }

    /**
     * 单生产者-多消费者
     */
    public void send(String msg) {
        String sendMsg = msg + new Date();
        System.out.println("Sender1 : " + sendMsg);
        this.rabbitTemplate.convertAndSend("hello", sendMsg);
    }

    public void sendUser() {
        this.rabbitTemplate.convertAndSend("user", new User());
    }

    public void sendY() {
        this.rabbitTemplate.convertAndSend("we", "你是智障吗？");

    }

}
