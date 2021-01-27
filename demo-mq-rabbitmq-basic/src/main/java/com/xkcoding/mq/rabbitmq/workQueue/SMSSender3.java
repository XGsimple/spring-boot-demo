package com.xkcoding.mq.rabbitmq.workQueue;

import com.rabbitmq.client.*;
import com.xkcoding.mq.rabbitmq.utils.RabbitConstant;
import com.xkcoding.mq.rabbitmq.utils.RabbitUtils;

import java.io.IOException;

/**
 * @author xugangq
 * @description 消费者
 * @createTime 2021-01-24 18:52
 */
public class SMSSender3 {
    public static void main(String[] args) throws IOException {
        Connection conn = RabbitUtils.getConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_SMS, false, false, false, null);
        //如果不写 basicQos(1)，则MQ自动将所有请求平分给所有消费者
        //basicQos,MQ不再对消费者一次发送多个请求，而是消费者处理完一个消息后（确认后basicAck），再从队列中获取一个新的消息
        channel.basicQos(1); //处理完一个取一个,不设置的话，则会轮询将消息发送给消费者，而没考虑消费者的处理能力
        channel.basicConsume(RabbitConstant.QUEUE_SMS, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String jsonSMS = new String(body);
                System.out.println("SMSSender1-短信发送成功" + jsonSMS);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
