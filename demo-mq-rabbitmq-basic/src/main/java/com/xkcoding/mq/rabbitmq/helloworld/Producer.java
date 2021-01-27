package com.xkcoding.mq.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xkcoding.mq.rabbitmq.utils.RabbitConstant;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xugangq
 * @description
 * @createTime 2021-01-24 18:08
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.101.165.21");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("/");
        //获取TCP长连接
        Connection conn = connectionFactory.newConnection();
        //创建通道"信道"，相当于TCP中的虚拟连接
        Channel channel = conn.createChannel();
        // 创建队列 , 声明并创建一个队列 , 如果队列已存在 , 则使用这个队列
        // 第一个参数 : 队列名称 ID
        // 第二个参数 : 是否持久化 , false 对应不持久化数据 , MQ 停掉数据就会丢失
        // 第三个参数 : 是否队列私有化 , false 则代表所有消费者都可以访问 , true 代表只有第一次拥有它的消费者才能一直使用
        // 第四个 : 是否自动删除 , false 代表连接停掉后不自动删除掉这个队列
        // 其他额外的参数 , nu11
        channel.queueDeclare(RabbitConstant.QUEUE_HELLOWORLD, false, false, false, null);
        String message = "hello world";
        // 四个参数
        // exchange   交换机,暂时用不到,在后面进行发布订阅时才会用到
        // 队列名称
        // 额外的设置属性
        // 最后一个参数是要传递的消息字节数组
        channel.basicPublish("", RabbitConstant.QUEUE_HELLOWORLD, null, message.getBytes());
        channel.close();
        conn.close();
        System.out.println("=====发送成功====");


    }
}
