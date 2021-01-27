package com.xkcoding.mq.rabbitmq.pubsub;

import com.rabbitmq.client.*;
import com.xkcoding.mq.rabbitmq.utils.RabbitConstant;
import com.xkcoding.mq.rabbitmq.utils.RabbitUtils;

import java.io.IOException;

/**
 * @author xugangq
 * @description 订阅者
 * @createTime 2021-01-24 19:27
 */
public class Baidu {
    public static void main(String[] args) throws IOException {
        Connection conn = RabbitUtils.getConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_BAIDU, false, false, false, null);

        channel.queueBind(RabbitConstant.QUEUE_BAIDU, RabbitConstant.EXCHANG_WEATHER, "");
        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_BAIDU, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("新浪天气收到气象信息：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
