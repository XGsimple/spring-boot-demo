package com.xkcoding.orm.mybatis.plus.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xkcoding.orm.mybatis.plus.SpringBootDemoOrmMybatisPlusApplicationTests;
import com.xkcoding.orm.mybatis.plus.entity.User;
import com.xkcoding.orm.mybatis.plus.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * User Service 测试
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-08 18:13
 */
@Slf4j
public class UserServiceTest extends SpringBootDemoOrmMybatisPlusApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 测试Mybatis-Plus 新增
     */
    @Test
    public void testSave() {
        String salt = IdUtil.fastSimpleUUID();
        User testSave3 = User.builder().name("testSave3").password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave3@xkcoding.com").phoneNumber("17300000003").status(1).lastLoginTime(new DateTime()).build();
        boolean save = userService.save(testSave3);
        Assert.assertTrue(save);
        log.debug("【测试id回显#testSave3.getId()】= {}", testSave3.getId());
    }

    /**
     * 测试Mybatis-Plus 批量新增
     */
    @Test
    public void testSaveList() {
        List<User> userList = Lists.newArrayList();
        for (int i = 0; i < 21; i++) {
            String salt = IdUtil.fastSimpleUUID();
            User user = User.builder().name("user_" + i).age(RandomUtil.randomInt(10, 100)).password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave" + i + "@xkcoding.com").phoneNumber("1730000000" + i).status(1).build();
            userList.add(user);
        }
        boolean batch = userService.saveBatch(userList);
        Assert.assertTrue(batch);
        List<Long> ids = userList.stream().map(User::getId).collect(Collectors.toList());
        log.debug("【userList#ids】= {}", ids);
    }

    /**
     * 测试Mybatis-Plus 删除
     */
    @Test
    public void testDelete() {
        boolean remove = userService.removeById(1L);
        Assert.assertTrue(remove);
        User byId = userService.getById(1L);
        Assert.assertNull(byId);
    }

    /**
     * 测试Mybatis-Plus 修改
     */
    @Test
    public void testUpdate() {
        User user = userService.getById(1L);
        Assert.assertNotNull(user);
        user.setName("MybatisPlus修改名字");
        boolean b = userService.updateById(user);
        Assert.assertTrue(b);
        User update = userService.getById(1L);
        Assert.assertEquals("MybatisPlus修改名字", update.getName());
        log.debug("【update】= {}", update);
    }

    /**
     * 测试Mybatis-Plus 查询单个
     */
    @Test
    public void testQueryOne() {
        User user = userService.getById(1L);
        Assert.assertNotNull(user);
        log.debug("【user】= {}", user);
    }

    /**
     * 测试Mybatis-Plus 查询全部
     */
    @Test
    public void testQueryAll() {
        List<User> list = userService.list(new QueryWrapper<>());
        Assert.assertTrue(CollUtil.isNotEmpty(list));
        log.debug("【list】= {}", list);
    }

    /**
     * 测试Mybatis-Plus 分页排序查询
     */
    @Test
    public void testQueryByPageAndSort() {
        initData();
        int count = userService.count(new QueryWrapper<>());
        Page<User> userPage = new Page<>(1, 5);
        userPage.setDesc("id");
        IPage<User> page = userService.page(userPage, new QueryWrapper<>());
        Assert.assertEquals(5, page.getSize());
        Assert.assertEquals(count, page.getTotal());
        log.debug("【page.getRecords()】= {}", page.getRecords());
    }

    /**
     * 测试Mybatis-Plus 自定义查询
     */
    @Test
    public void testQueryByCondition() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "Save1").or().eq("phone_number", "17300000001").orderByDesc("id");
        int count = userService.count(wrapper);
        Page<User> userPage = new Page<>(1, 3);
        IPage<User> page = userService.page(userPage, wrapper);
        Assert.assertEquals(3, page.getSize());
        Assert.assertEquals(count, page.getTotal());
        log.debug("【page.getRecords()】= {}", page.getRecords());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        testSaveList();
    }

    //=============================================================================================================

    /**
     * 使用LambdaQueryWrapper
     * SELECT name,age FROM orm_user WHERE name LIKE 'user%' AND age < 40 OR age > 50
     */
    @Test
    public void lambdaQueryWrapperTest() {
        //有多种方式获得 随便记住一种即可;
        //不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        //LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>(user);
        //LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        //LambdaQueryChainWrapper<User> lambdaQueryWrapper = userService.lambdaQuery();

        //lambdaQueryWrapper.select(User::getName, User::getAge).likeRight(User::getName, "user").lt(User::getAge, 40).or().gt(User::getAge, 50);
        lambdaQueryWrapper.likeRight(User::getName, "user").lt(User::getAge, 40).or().gt(User::getAge, 50);
        List<User> list = userService.list(lambdaQueryWrapper);
        log.debug("【list】= {}", list);

        //区间查询

    }


    /**
     * between区间查询，查找用户年龄在20-30的用户
     */
    @Test
    public void wrapper1Test() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.select(User::getName);
        lambdaQueryWrapper.between(User::getAge, 20, 30);//区间
        int count = userService.count(lambdaQueryWrapper);
        log.debug("【between】{}人 : 20到30年龄区间", count);
    }

    /**
     * id在子查询中查出来
     * SELECT name,age
     * FROM orm_user
     * WHERE deleted=0 AND (id IN (select id from user where id < 3))
     */
    @Test
    public void wrapper2Test() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.select(User::getName, User::getAge);
        lambdaQueryWrapper.inSql(User::getId, "select id from user where id < 3");
        List<User> users = userService.list(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * func 方法(主要方便在出现if...else下调用不同方法能不断链)
     * SELECT name,age FROM orm_user WHERE deleted=0 AND (age = 22)
     */
    @Test
    public void wrapper3Test() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.select(User::getName, User::getAge);
        boolean condition = true;
        lambdaQueryWrapper.func(i -> {
                if (condition) {
                    i.eq(User::getAge, 22);
                } else {
                    i.eq(User::getAge, 33);
                }
            }
        );
        List<User> users = userService.list(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * nested嵌套
     * SELECT name,age
     * FROM orm_user
     * WHERE deleted=0 AND (name LIKE ? OR (age > ? AND deleted = ?))
     */
    @Test
    public void wrapper4Test() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.select(User::getName, User::getAge);
        lambdaQueryWrapper
            .likeRight(User::getName, "user")
            .or()
            .nested(i -> i.gt(User::getAge, 50).eq(User::getDeleted,0));
        List<User> users = userService.list(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     *apply 拼接sql
     * 该方法可用于数据库函数 动态入参的params对应前面applySql内部的{index}部分.
     * 这样是不会有sql注入风险的,反之会有!
     *SELECT name,age
     *FROM orm_user
     *WHERE deleted=0 AND (date_format(create_time,'%Y-%m-%d')=‘2020-12-24’)
     */
    @Test
    public void wrapper5Test() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.select(User::getName, User::getCreateTime);
        lambdaQueryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}","2020-12-23");
        List<User> users = userService.list(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * select的字段筛选
     * 过滤查询的字段信息(主键除外!)
     * 例1: 只要 java 字段名以 "test" 开头的 -> select(i -> i.getProperty().startsWith("test"))
     * 例2: 只要 java 字段属性是 CharSequence 类型的 -> select(TableFieldInfo::isCharSequence)
     * 例3: 只要 java 字段没有填充策略的 -> select(i -> i.getFieldFill() == FieldFill.DEFAULT)
     * 例4: 要全部字段 -> select(i -> true)
     * 例5: 只要主键字段 -> select(i -> false)
     */
    @Test
    public void wrapper6Test() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.select(User.class,i->i.getFieldFill()== FieldFill.DEFAULT);
        List<User> users = userService.list(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 自定义sql
     */
    @Test
    public void wrapper7Test() {
        User user = userMapper.myGetUserById(1L);
        List<User> users = userMapper.mySelectAll();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name").eq("age",22);
        List<User> usersByWrapper = userMapper.mySelectByMyWrapper(queryWrapper);

        log.debug("【user】= {}", user);
        log.debug("【users】= {}", users);
        log.debug("【usersByWrapper】= {}", usersByWrapper);

        //UPDATE orm_user SET email = 'update@user.com' WHERE (age = 14)
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        User updateUser = new User();
        updateUser.setEmail("update@user.com");
        updateWrapper.eq("age",14);
        userMapper.myUpdateByMyWrapper(updateWrapper,updateUser);
    }

    /**
     * 测试乐观锁成功案例
     * 单线程情况，更新操作肯定成功
     * 同时version加1
     */
    @Test
    public void optimisticLockerInterceptor1Test() {
        //1、查询用户
        User user = userService.getById(1L);
        //2、修改用户信息
        user.setAge(10);
        //3、执行更新操作
        userService.updateById(user);
    }

    /**
     * 测试乐观锁成功案例
     * 多线程情况
     */
    @Test
    public void optimisticLockerInterceptorMultiTest() throws InterruptedException {
        final int THREAD_SIZE = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_SIZE);
        //1、查询用户
        User user = userService.getById(1L);
        //2、修改用户信息
        user.setAge(20);
        //3、多线程并发执行更新操作
        for (int i = 0; i < THREAD_SIZE; i++) {
            executorService.execute(() -> userService.updateById(user));
        }
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 分页测试
     */
    @Test
    public void pageTest() {
        /**
         * 构造参数：查询第一页，每页条数为5条
         */
        Page<User> userPage = new Page<>(1, 5);

        //查询的条件用实体类接收，user什么都没设置则默认查询所有
        User queryUser = new User();
        queryUser.setVersion(1);

        //TODO 如果属性不一致，需要做特殊处理
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery(queryUser);

        //查询
        Page<User> page = userService.page(userPage, queryWrapper);

        //转成json输出，需要引入hutool工具类依赖
        String jsonString = JSONUtil.parse(page).toJSONString(1);
        log.info("查询的结果 = {}", jsonString);
    }

    /**
     * 逻辑删除
     * 配置文件中：
     * logic-delete-value: 1 # 逻辑已删除值(默认为 1)
     * logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
     * 实体类的逻辑删除字段上添加注解    @TableLogic
     */
    @Test
    public void logicDeleteTest() {
        User user = new User();
        user.setId(11L);
        //查询的条件用实体类接收，user什么都没设置则默认查询所有
        User queryUser = new User();
        queryUser.setId(1L);

        //TODO 如果属性不一致，需要做特殊处理
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery(queryUser);

        boolean remove = userService.remove(wrapper);
        log.info("delete={}", remove);
    }

}
