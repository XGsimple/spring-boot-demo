package com.xkcoding.utils;

import com.github.javafaker.Color;
import com.github.javafaker.Faker;
import com.github.javafaker.University;
import com.xkcoding.component.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class RandomUtilTest {

    @Test
    void testNextObject() {
        User person = RandomUtil.nextObject(User.class);
        assertEquals("result", RandomUtil.nextObject(String.class));
    }

    @Test
    void testNextList() {
        assertEquals(Arrays.asList("value"), RandomUtil.nextList(String.class, 0));
        assertEquals(Collections.emptyList(), RandomUtil.nextList(String.class, 0));
    }

    @Test
    void testNextSet() {
        assertEquals(new HashSet<>(Arrays.asList("value")), RandomUtil.nextSet(String.class, 0));
        assertEquals(Collections.emptySet(), RandomUtil.nextSet(String.class, 0));
    }

    @Test
    void testObjects() {
        // Setup
        // Run the test
        final Stream<String> result = RandomUtil.objects(String.class, 0);

        // Verify the results
    }

    @Test
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
