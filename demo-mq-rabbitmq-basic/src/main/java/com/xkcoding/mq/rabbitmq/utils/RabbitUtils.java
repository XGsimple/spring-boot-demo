package com.xkcoding.mq.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * @author xugangq
 * @description
 * @createTime 2021-01-24 18:23
 */
public class RabbitUtils {
    public static ConnectionFactory connectionFactory = new ConnectionFactory();

    static {
        connectionFactory.setHost("47.101.165.21");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("/");
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = connectionFactory.newConnection();
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
