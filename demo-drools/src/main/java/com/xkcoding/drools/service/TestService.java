package com.xkcoding.drools.service;

import com.xkcoding.drools.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TestService
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-08-01
 */
@Slf4j
@Service
public class TestService {

    @Autowired
    private KieSession kieSession;

    public void testPerson() {
        Person person = new Person();
        person.setName("LeifChen");
        person.setAge(18);
        kieSession.insert(person);
        int count = kieSession.fireAllRules();
        log.info("总执行了{}条规则", count);
    }
}
