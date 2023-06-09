package com.xkcoding.bf.controller;

import com.xkcoding.bf.helper.BloomFilterHelper;
import com.xkcoding.bf.helper.RedisBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xugangq
 * @date 2021/6/17 20:13
 */
@Controller
public class TestController {
    @Autowired
    RedisBloomFilter redisBloomFilter;

    @Autowired
    private BloomFilterHelper bloomFilterHelper;

    @ResponseBody
    @RequestMapping("/add")
    public String addBloomFilter(@RequestParam("orderNum") String orderNum) {
        try {
            redisBloomFilter.addByBloomFilter(bloomFilterHelper, "bloom", orderNum);
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }

    @ResponseBody
    @RequestMapping("/check")
    public boolean checkBloomFilter(@RequestParam("orderNum") String orderNum) {
        boolean b = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, "bloom", orderNum);
        return b;
    }
}
