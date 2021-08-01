package com.xkcoding.controller;

import com.xkcoding.component.R;
import com.xkcoding.component.User;
import com.xkcoding.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author MintLemon
 * @description
 * @createTime 2021-08-01 21:09
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户
     *
     * @param id
     */
    @GetMapping("/query/{id}")
    @ResponseBody
    public R queryUser(@PathVariable(value = "id") Integer id) {
        User user = userService.queryUserById(id);
        return R.ok().put("user", user);
    }

    /**
     * 新添用户
     *
     * @param user
     */
    @PostMapping("/save")
    @ResponseBody
    public R saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return R.ok();
    }

    /**
     * 修改用户
     *
     * @param user
     */
    @PostMapping("/update")
    @ResponseBody
    public R updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return R.ok();
    }

    /**
     * 删除用户
     *
     * @param ids
     */
    @PostMapping("/remove")
    @ResponseBody
    public R removeBatch(@RequestBody Integer[] ids) {
        userService.removeBatch(ids);
        return R.ok();
    }
}
