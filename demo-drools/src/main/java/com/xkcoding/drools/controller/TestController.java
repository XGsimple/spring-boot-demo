package com.xkcoding.drools.controller;

import com.xkcoding.drools.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-08-01
 */
@RestController
@RequestMapping("/drools")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    /**
     * 测试Person
     */
    @GetMapping("/testPerson")
    public void testPerson() {
        testService.testPerson();
    }
}
