package com.xkcoding.drools.service;

import com.xkcoding.drools.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 运算符单元测试
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-09-01
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OperatorTest {

    @Autowired
    private KieBase kieBase;
    private KieSession kieSession;

    @BeforeEach
    public void before() {
        Person person = new Person();
        person.setAge(18);

        kieSession = kieBase.newKieSession();
        kieSession.insert(person);
    }

    @AfterEach
    public void after() {
        kieSession.dispose();
    }

    @Test
    public void testAdd() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testAdd"))).isEqualTo(1);
    }

    @Test
    public void testSub() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testSub"))).isEqualTo(1);
    }

    @Test
    public void testMul() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testMul"))).isEqualTo(1);
    }

    @Test
    public void testDiv() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testDiv"))).isEqualTo(1);
    }

    @Test
    public void testMod() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testMod"))).isEqualTo(1);
    }
}