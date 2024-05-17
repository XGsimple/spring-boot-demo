package com.xkcoding.junit.controller;

import com.xkcoding.junit.component.R;
import com.xkcoding.junit.entity.OrmUser;
import com.xkcoding.junit.service.OrmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Boot Demo Orm 系列示例表(OrmUser)表控制层
 *
 * @author makejava
 * @since 2024-05-17 17:11:47
 */
@RestController
@RequestMapping("/user")
public class OrmUserController {
    @Autowired
    private OrmUserService userService;

    /**
     * 获取用户
     *
     * @param id
     */
    @GetMapping("/query/{id}")
    public R queryUser(@PathVariable(value = "id") Integer id) {
        OrmUser user = userService.getById(id);
        return R.ok().put("user", user);
    }

    /**
     * 新添用户
     *
     * @param user
     */
    @PostMapping("/save")
    public R saveUser(@RequestBody OrmUser user) {
        userService.save(user);
        return R.ok();
    }

    /**
     * 修改用户
     *
     * @param user
     */
    @PostMapping("/update")
    public R updateUser(@RequestBody OrmUser user) {
        userService.updateById(user);
        return R.ok();
    }

    /**
     * 删除用户
     *
     * @param ids
     */
    @PostMapping("/remove")
    public R removeBatch(@RequestBody Integer[] ids) {
        userService.removeById(ids);
        return R.ok();
    }
}

