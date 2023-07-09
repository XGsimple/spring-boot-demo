package com.xkcoding.bf.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Component
public class RedisDelayService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private final String ORDER_KEY = "study:delay:OrderId";

    //生产者,生成5个订单放进去
    public void productionDelayMessage() {
        for (int i = 0; i < 5; i++) {
            //延迟3秒
            LocalDateTime second3laterDateTime = LocalDateTime.now().plus(3, ChronoUnit.SECONDS);
            long second3later = second3laterDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            BoundZSetOperations<String, String> zSetOperations = stringRedisTemplate.boundZSetOps("study:delay:OrderId");
            zSetOperations.add("OID0000001" + i, second3later);
            System.out.println(System.currentTimeMillis() + "ms:redis生成了一个订单任务：订单ID为" + "OID0000001" + i);
        }
    }

    //消费者，取订单
    public void consumerDelayMessage() {
        BoundZSetOperations<String, String> zSetOperations = stringRedisTemplate.boundZSetOps(ORDER_KEY);

        while (true) {
            Set<ZSetOperations.TypedTuple<String>> items = zSetOperations.rangeWithScores(0, 1);
            if (items == null || items.isEmpty()) {
                System.out.println("当前没有等待的任务");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }
            ZSetOperations.TypedTuple<String> firstTypedTuple = (ZSetOperations.TypedTuple<String>)(items.toArray()[0]);
            Double score = firstTypedTuple.getScore();
            long nowMilli = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if (nowMilli >= score) {
                String orderId = firstTypedTuple.getValue();
                //多线程处理，防止重复消费
                //在remove前，可能被多个线程拿到
                Long num = zSetOperations.remove(orderId);
                if (num != null && num > 0) {
                    System.out.println(
                        System.currentTimeMillis() + "ms:redis消费了一个任务：消费的订单OrderId为" + orderId);
                }
            }
        }
    }

    public static void main(String[] args) {
        RedisDelayService appTest = new RedisDelayService();
        appTest.productionDelayMessage();
        appTest.consumerDelayMessage();
    }
}
