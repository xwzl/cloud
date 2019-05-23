package com.cloud.order.message.test;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 若要实现消息回调，必须实现这个类
 *
 * @author xuweizhi
 * @date 2019/05/23 10:09
 */
@Component
@Slf4j
public class CallBackSender implements RabbitTemplate.ConfirmCallback {

    /**
     * 消息队列回调
     */
    private RabbitTemplate rabbitTemplate;

    public CallBackSender(@NotNull RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // 这里必须设置为 true
        rabbitTemplate.setConfirmCallback(this);
    }

    public void send() {
        String msg = "callbackSender : i am callback sender";
        log.info(msg);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        System.out.println("callbackSender UUID: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend("exchange", "topic.messages", msg, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // TODO Auto-generated method stub
        if (correlationData != null) {
            System.out.println("callbakck confirm: " + correlationData.getId());
        }
    }


}