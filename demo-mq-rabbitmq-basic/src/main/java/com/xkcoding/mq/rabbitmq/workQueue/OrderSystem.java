package com.xkcoding.mq.rabbitmq.workQueue;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xkcoding.mq.rabbitmq.utils.RabbitConstant;
import com.xkcoding.mq.rabbitmq.utils.RabbitUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author xugangq
 * @description
 * @createTime 2021-01-24 18:38
 */
public class OrderSystem {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection conn = RabbitUtils.getConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_SMS, false, false, false, null);

        ExecutorService pool = Executors.newFixedThreadPool(6);
        CountDownLatch countDownLatch = new CountDownLatch(20);
        AtomicInteger userId = new AtomicInteger(0);
        IntStream.range(0, 20).forEach(num -> pool.execute(() -> {
            for (int i = 0; i <= 1000; i++) {
                String threadName = Thread.currentThread().getName();
                SMS sms = new SMS("线程" + threadName + "：乘客" + userId.getAndIncrement(), "13900000" + i, "你的车票已预订成功");
                String jsonSMS = JSON.toJSONString(sms);
                try {
                    channel.basicPublish("", RabbitConstant.QUEUE_SMS, null, jsonSMS.getBytes());
                    System.out.println(threadName+"===="+userId.get()+":发送数据成功");
                    //TimeUnit.MILLISECONDS.sleep(RandomUtil.randomInt(100,2000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        }));
        countDownLatch.await();
        channel.close();
        conn.close();
    }
}
