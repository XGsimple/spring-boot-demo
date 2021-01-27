package com.xkcoding.mq.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xkcoding.mq.rabbitmq.utils.RabbitConstant;
import com.xkcoding.mq.rabbitmq.utils.RabbitUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author xugangq
 * @description 发布者
 * @createTime 2021-01-24 19:22
 */
public class WeatherBureau {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection conn = RabbitUtils.getConnection();
        String input = new Scanner(System.in).next();
        Channel channel = conn.createChannel();
        //第一个参数是交换机的名字
        channel.basicPublish(RabbitConstant.EXCHANG_WEATHER,"",null,input.getBytes());

        channel.close();
        conn.close();
    }
}
