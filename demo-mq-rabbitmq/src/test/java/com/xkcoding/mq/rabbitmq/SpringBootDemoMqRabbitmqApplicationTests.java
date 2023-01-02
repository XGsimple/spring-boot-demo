package com.xkcoding.mq.rabbitmq;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.xkcoding.mq.rabbitmq.constants.RabbitConsts;
import com.xkcoding.mq.rabbitmq.message.MessageStruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@SpringBootTest
public class SpringBootDemoMqRabbitmqApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试直接模式发送1
     */
    @Test
    public void testMessageProperties() throws Exception {
        //创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述");
        messageProperties.getHeaders().put("type", "自定义消息类型");
        messageProperties.setContentType("text/plain");
        Message message = new Message("Hello RabbitMQ".getBytes(), messageProperties);
        //发送
        rabbitTemplate.convertAndSend(RabbitConsts.DIRECT_MODE_QUEUE_ONE, message);
        TimeUnit.SECONDS.sleep(3);
    }

    /**
     * 测试直接模式发送2
     */
    @Test
    public void sendDirect() throws InterruptedException {
        rabbitTemplate.convertAndSend(RabbitConsts.DIRECT_MODE_QUEUE_ONE, new MessageStruct("direct message"));
        TimeUnit.SECONDS.sleep(3);
    }

    /**
     * 测试分列模式发送
     */
    @Test
    public void sendFanout() {
        rabbitTemplate.convertAndSend(RabbitConsts.FANOUT_MODE_QUEUE, "", new MessageStruct("fanout message"));
    }

    /**
     * 测试主题模式发送1
     */
    @Test
    public void sendTopic1() {
        rabbitTemplate.convertAndSend(RabbitConsts.TOPIC_MODE_QUEUE, "queue.aaa.bbb", new MessageStruct("topic message"));
    }

    /**
     * 测试主题模式发送2
     */
    @Test
    public void sendTopic2() {
        rabbitTemplate.convertAndSend(RabbitConsts.TOPIC_MODE_QUEUE, "ccc.queue", new MessageStruct("topic message"));
    }

    /**
     * 测试主题模式发送3
     */
    @Test
    public void sendTopic3() {
        rabbitTemplate.convertAndSend(RabbitConsts.TOPIC_MODE_QUEUE, "3.queue", new MessageStruct("topic message"));
    }

    /**
     * 测试延迟队列发送
     */
    @Test
    public void sendDelay() {
        rabbitTemplate.convertAndSend(RabbitConsts.DELAY_MODE_QUEUE,
                                      RabbitConsts.DELAY_QUEUE,
                                      new MessageStruct("delay message, delay 5s, " + DateUtil.date()),
                                      message -> {
                                          message.getMessageProperties().setHeader("x-delay", 5000);
                                          return message;
                                      });
        rabbitTemplate.convertAndSend(RabbitConsts.DELAY_MODE_QUEUE,
                                      RabbitConsts.DELAY_QUEUE,
                                      new MessageStruct("delay message,  delay 2s, " + DateUtil.date()),
                                      message -> {
                                          message.getMessageProperties().setHeader("x-delay", 2000);
                                          return message;
                                      });
        rabbitTemplate.convertAndSend(RabbitConsts.DELAY_MODE_QUEUE,
                                      RabbitConsts.DELAY_QUEUE,
                                      new MessageStruct("delay message,  delay 8s, " + DateUtil.date()),
                                      message -> {
                                          message.getMessageProperties().setHeader("x-delay", 8000);
                                          return message;
                                      });
    }

    /**
     * 测试延迟队列发送
     */
    @Test
    public void sendDelay2() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            //id + 时间戳 全局唯一
            String msg = "编号" + i + ":" + new Date();
            CorrelationData correlationData = new CorrelationData(msg);
            //发送消息时指定 header 延迟时间
            rabbitTemplate.convertAndSend(RabbitConsts.DELAY_MODE_QUEUE, RabbitConsts.DELAY_QUEUE, new MessageStruct(msg), message -> {
                //设置消息持久化
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                //message.getMessageProperties().setHeader("x-delay", "6000");
                message.getMessageProperties().setDelay(RandomUtil.randomInt(2, 10) * 1000);
                return message;
            }, correlationData);

        }

        TimeUnit.SECONDS.sleep(15);
    }

}

