package com.cloud.order.message.test;

import org.apache.catalina.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xuweizhi
 * @date 2019/05/23 10:09
 */
@Component
public class HelloReceiver1 {

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver1  : " + hello);
    }

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void process2(String hello) {
        System.out.println("Receiver2  : " + hello);
    }

    @RabbitListener(queues = "user")
    @RabbitHandler
    public void process2(@NotNull User user) {
        System.out.println("Receiver2  : " + user.toString());
    }

    @RabbitListener(queues = "we")
    @RabbitHandler
    public void process3(String we) {
        System.out.println("Receiver2  : " + we);
    }

}