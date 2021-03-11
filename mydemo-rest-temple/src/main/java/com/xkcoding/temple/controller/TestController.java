package com.xkcoding.temple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/4 15:28
 */
@Controller
public class TestController {
    /**
     * @GetMapping("testRestGet") 当前方法只接受Get请求
     * 等价于
     * @RequestMapping(path = "testRestGet",method = RequestMethod.GET)
     */
    @GetMapping("testRestGet")
    @ResponseBody
    public String testRestGet(String username){
        return "这是一个Get请求，接受参数："+username;
    }

    /**
     * @PostMapping("") 当前方法只接受Post请求
     * 等价于
     * @RequestMapping(path = "testRestPost",method = RequestMethod.POST)
     */
    @PostMapping("testRestPost")
    @ResponseBody
    public String testRestPost(String username){
        return "这是一个Post请求，接受参数："+username;
    }

    /**
     * 测试 postForLocation 给RestTemplate响应url地址
     */
    @PostMapping("testRestPostLocation")
    public String testRestPostLocation(String username){
        System.out.println(username);
        return "redirect:/success.html";
    }
}
