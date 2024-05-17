package com.xkcoding.junit.utils;

import com.github.javafaker.Color;
import com.github.javafaker.Faker;
import com.github.javafaker.University;
import com.xkcoding.junit.component.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://blog.csdn.net/dadiyang/article/details/109703348?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-109703348-blog-89390407.235%5Ev28%5Epc_relevant_t0_download&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-109703348-blog-89390407.235%5Ev28%5Epc_relevant_t0_download
 */
@Slf4j
class RandomUtilTest {

    @Test
    @DisplayName("测试easyRandom——根据给定的类型生成一个随机的对象")
    void testNextObject() {
        User person = RandomUtil.nextObject(User.class);
        assertEquals("result", RandomUtil.nextObject(String.class));
    }

    @Test
    @DisplayName("测试easyRandom——根据给定的类型和大小生成一个随机对象的列表")
    void testNextList() {
        assertEquals(Arrays.asList("value"), RandomUtil.nextList(String.class, 0));
        assertEquals(Collections.emptyList(), RandomUtil.nextList(String.class, 0));
    }

    @Test
    @DisplayName("测试easyRandom——根据给定的类型和大小生成一个随机对象的集合")
    void testNextSet() {
        assertEquals(new HashSet<>(Arrays.asList("value")), RandomUtil.nextSet(String.class, 0));
        assertEquals(Collections.emptySet(), RandomUtil.nextSet(String.class, 0));
    }

    @Test
    @DisplayName("测试easyRandom—— 根据给定的类型和大小生成一个随机对象流")
    void testObjects() {
        // Setup
        // Run the test
        final Stream<String> result = RandomUtil.objects(String.class, 0);

        // Verify the results
    }

    @Test
    @DisplayName("测试JavaFaker")
    void testJavaFaker() {
        Faker faker = new Faker(new Locale("zh-CN"));
        //姓名
        String name = faker.name().fullName();
        log.info(name);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        log.info(lastName, firstName);
        //街道
        String streetAddress = faker.address().streetAddress();
        log.info(streetAddress);
        //颜色
        Color color = faker.color();
        log.info(color.name() + "-->" + color.hex());
        // 大学
        University university = faker.university();
        log.info(university.name() + "-->" + university.prefix() + "" + university.suffix());
    }
}
