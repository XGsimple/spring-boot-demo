package com.xkcoding.mq.rabbitmq.helloworld;


import com.rabbitmq.client.*;
import com.xkcoding.mq.rabbitmq.utils.RabbitConstant;
import com.xkcoding.mq.rabbitmq.utils.RabbitUtils;

import java.io.IOException;

/**
 * @author xugangq
 * @description
 * @createTime 2021-01-24 18:22
 */
public class Consumer {
    public static void main(String[] args) throws IOException {
        //获取TCP长连接
        Connection conn = RabbitUtils.getConnection();
        //创建通道"信道"，相当于TCP中的虚拟连接
        Channel channel = conn.createChannel();
        // 创建队列 , 声明并创建一个队列 , 如果队列已存在 , 则使用这个队列
        // 第一个参数 : 队列名称 ID
        // 第二个参数 : 是否持久化 , false 对应不持久化数据 , MQ 停掉数据就会丢失
        // 第三个参数 : 是否队列私有化 , false 则代表所有消费者都可以访问 , true 代表只有第一次拥有它的消费者才能一直使用
        // 第四个 : 是否自动删除 , false 代表连接停掉后不自动删除掉这个队列
        // 其他额外的参数 , nu11
        channel.queueDeclare(RabbitConstant.QUEUE_HELLOWORLD, false, false, false, null);
        // 创建一个消息消费者
        // 第一个参数 : 队列名
        // 第二个参数代表是否自动确认收到消息 , false 代表手动编程来确认消息 , 这是 MQ 的推荐做法
        // 第三个参数要传入 DefaultConsumer 的实现类
        channel.basicConsume(RabbitConstant.QUEUE_HELLOWORLD, false, new Receiver(channel));

    }

    static class Receiver extends DefaultConsumer {
        private Channel channel;

        public Receiver(Channel channel) {
            super(channel);
            this.channel = channel;
        }


        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            String message = new String(body);
            System.out.println("消费者接收到消息："+message);
            System.out.println("消费的TagId："+envelope.getDeliveryTag());
            //false只确认签收当前的消息，设为true则代表签收该消费者所有未签收的消息
            channel.basicAck(envelope.getDeliveryTag(),false);
        }
    }
}
