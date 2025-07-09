package com.xkcoding.drools.service;

import com.xkcoding.drools.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 规则属性单元测试
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-08-17
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AttributeTest {

    @Autowired
    private KieSession kieSession;

    @Test
    public void testNoLoop() {
        Person person = new Person();
        person.setName("no-loop");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules()).isEqualTo(1);
    }

    @Test
    public void testLockOnActive() {
        Person person = new Person();
        person.setName("lock-on-active");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules()).isEqualTo(1);
    }

    @Test
    public void testSalience() {
        Person person = new Person();
        person.setName("salience");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules()).isEqualTo(2);
    }

    @Test
    public void testEnabled() {
        Person person = new Person();
        person.setName("enabled");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testEnabledTrue"))).isEqualTo(1);
    }

    @Test
    public void testDateEffective() {
        Person person = new Person();
        person.setName("date-effective");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules()).isEqualTo(1);
    }

    @Test
    public void testDateExpires() {
        Person person = new Person();
        person.setName("date-expires");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules()).isEqualTo(1);
    }

    @Test
    public void testActivationGroup() {
        Person person = new Person();
        person.setName("activation-group");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules()).isEqualTo(1);
    }

    @Test
    public void testAgendaGroup() {
        Person person = new Person();
        person.setName("agenda-group");
        kieSession.insert(person);
        kieSession.getAgenda().getAgendaGroup("ag1").setFocus();
        assertThat(kieSession.fireAllRules()).isEqualTo(1);
    }

    @Test
    public void testAutoFocus() {
        Person person = new Person();
        person.setName("auto-focus");
        kieSession.insert(person);
        assertThat(kieSession.fireAllRules()).isEqualTo(1);
    }
}