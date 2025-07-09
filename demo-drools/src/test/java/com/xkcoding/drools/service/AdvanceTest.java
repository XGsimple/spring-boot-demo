package com.xkcoding.drools.service;

import com.xkcoding.drools.bean.Person;
import com.xkcoding.drools.bean.School;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 中级语法单元测试
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-09-02
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AdvanceTest {

    @Autowired
    private KieBase kieBase;
    private KieSession kieSession;

    @BeforeEach
    public void before() {
        kieSession = kieBase.newKieSession();
    }

    @AfterEach
    public void after() {
        kieSession.dispose();
    }

    @Test
    public void testFunction() {
        assertThat(1).isEqualTo(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testFunction")));
    }

    @Test
    public void testQuery() {
        kieSession.insert(Person.builder().name("LeifChen").age(18).build());
        kieSession.insert(Person.builder().name("Lucy").age(16).build());
        kieSession.insert(Person.builder().name("Lily").age(20).build());
        Object[] objects = new Object[] {"LeifChen"};
        QueryResults results = kieSession.getQueryResults("person age is 18 and name is LeifChen", objects);
        for (QueryResultsRow result : results) {
            Person person = (Person)result.get("person");
            log.info("符合查询条件的对象：{}", person);
        }
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testQuery"))).isEqualTo(1);
        assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void testDeclare() {
        assertThat(kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("testDeclare"))).isEqualTo(2);
    }

    @Test
    public void testGlobal() {
        // 原始global全局变量
        kieSession.setGlobal("count", 10);
        School school = new School();
        school.setCode("S0");
        kieSession.setGlobal("school", school);
        kieSession.setGlobal("list", new ArrayList<>());

        assertThat(kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("testGlobal"))).isEqualTo(2);

        // 执行规则后的global全局变量
        int updateCount = (int)kieSession.getGlobal("count");
        School updateSchool = (School)kieSession.getGlobal("school");
        List updateList = (List)kieSession.getGlobal("list");

        assertThat(updateCount).isEqualTo(10);
        assertThat(updateSchool.getCode()).isEqualTo("S1");
        assertThat(updateList.size()).isEqualTo(2);
    }
}
