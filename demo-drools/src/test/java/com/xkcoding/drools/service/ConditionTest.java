package com.xkcoding.drools.service;

import com.xkcoding.drools.bean.Person;
import com.xkcoding.drools.bean.School;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 条件关系单元测试
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-08-31
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ConditionTest {

    @Autowired
    private KieBase kieBase;
    private KieSession kieSession;

    @BeforeEach
    public void before() {
        School school = new School();
        school.setClassNameList(Arrays.asList("一班", "二班", "三班"));
        Person person = new Person();
        person.setName("LeifChen");
        person.setClassName("一班");

        kieSession = kieBase.newKieSession();
        kieSession.insert(school);
        kieSession.insert(person);
    }

    @AfterEach
    public void after() {
        kieSession.dispose();
    }

    @Test
    public void testContains() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testContains"))).isEqualTo(1);
    }

    @Test
    public void testMemberOf() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testMemberOf"))).isEqualTo(1);
    }

    @Test
    public void testMatches() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testMatches"))).isEqualTo(1);
    }

    @Test
    public void testSoundslike() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testSoundslike"))).isEqualTo(1);
    }

    @Test
    public void testStr() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testStr"))).isEqualTo(1);
    }
}
