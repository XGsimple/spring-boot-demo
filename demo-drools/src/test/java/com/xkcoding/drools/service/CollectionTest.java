package com.xkcoding.drools.service;

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
import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 集合单元测试
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-09-01
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CollectionTest {

    @Autowired
    private KieBase kieBase;
    private KieSession kieSession;

    @BeforeEach
    public void before() {
        School school = new School();
        school.setClassNameList(Arrays.asList("一班", "二班", "三班"));
        school.setClassNameSet(new HashSet<String>() {{
            add("一班");
            add("二班");
            add("三班");
        }});
        school.setClassNameMap(new HashMap<String, Integer>() {{
            put("一班", 1);
            put("二班", 2);
            put("三班", 3);
        }});

        kieSession = kieBase.newKieSession();
        kieSession.insert(school);
        kieSession.insert(Arrays.asList("一班", "二班", "三班"));
        kieSession.insert(new HashSet<String>() {{
            add("一班");
            add("二班");
            add("三班");
        }});
        kieSession.insert(new HashMap<String, Integer>() {{
            put("一班", 1);
            put("二班", 2);
            put("三班", 3);
        }});
    }

    @AfterEach
    public void after() {
        kieSession.dispose();
    }

    @Test
    public void testList() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testList"))).isEqualTo(1);
    }

    @Test
    public void testSet() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testSet"))).isEqualTo(1);
    }

    @Test
    public void testMap() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testMap"))).isEqualTo(1);
    }

    @Test
    public void testCollectionList() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testCollectionList"))).isEqualTo(1);
    }

    @Test
    public void testCollectionSet() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testCollectionSet"))).isEqualTo(1);
    }

    @Test
    public void testCollectionMap() {
        assertThat(kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("testCollectionMap"))).isEqualTo(1);
    }
}
