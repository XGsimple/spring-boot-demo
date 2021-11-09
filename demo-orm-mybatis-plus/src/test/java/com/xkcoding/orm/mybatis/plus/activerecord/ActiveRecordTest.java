package com.xkcoding.orm.mybatis.plus.activerecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xkcoding.orm.mybatis.plus.SpringBootDemoOrmMybatisPlusApplicationTests;
import com.xkcoding.orm.mybatis.plus.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Role
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019-09-14 14:19
 */
@Slf4j
public class ActiveRecordTest extends SpringBootDemoOrmMybatisPlusApplicationTests {
    /**
     * 测试 ActiveRecord 插入数据
     */
    @Test
    public void testActiveRecordInsert() {
        Role role = new Role();
        role.setName("VIP");
        assertThat(role.insert()).isTrue();
        // 成功直接拿会写的 ID
        log.debug("【role】= {}", role);
    }

    /**
     * 测试 ActiveRecord 更新数据
     */
    @Test
    public void testActiveRecordUpdate() {
        assertThat(new Role().setId(1L).setName("管理员-1").updateById()).isTrue();
        assertThat(new Role().update(new UpdateWrapper<Role>().lambda().set(Role::getName, "普通用户-1").eq(Role::getId, 2))).isTrue();
    }

    /**
     * 测试 ActiveRecord 查询数据
     */
    @Test
    public void testActiveRecordSelect() {
        assertThat(new Role().setId(1L).selectById().getName()).isEqualTo("管理员");
        Role role = new Role().selectOne(new QueryWrapper<Role>().lambda().eq(Role::getId, 2));
        assertThat(role.getName()).isEqualTo("普通用户");
        List<Role> roles = new Role().selectAll();
        assertThat(roles.size() > 0).isTrue();
        log.debug("【roles】= {}", roles);
    }

    /**
     * 测试 ActiveRecord 删除数据
     */
    @Test
    public void testActiveRecordDelete() {
        assertThat(new Role().setId(1L).deleteById()).isTrue();
        assertThat(new Role().delete(new QueryWrapper<Role>().lambda().eq(Role::getName, "普通用户"))).isTrue();
    }
}
