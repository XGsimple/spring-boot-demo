package com.xkcoding.elasticsearch.web;

import com.xkcoding.elasticsearch.pojo.PageResult;
import com.xkcoding.elasticsearch.pojo.RequestParams;
import com.xkcoding.elasticsearch.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * http://localhost:8089/
 * 【黑马java】ElasticSearch教程入门到精通
 * https://www.bilibili.com/video/BV1Gh411j7d6?p=39&vd_source=c5fad4738e0db0fa62d750af372b4d5f
 */
@RestController
@RequestMapping("hotel")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    /**
     * 黑马旅游案例-搜索、分页、附近的酒店、广告置顶
     *
     * @param params
     * @return
     */
    @PostMapping("list")
    public PageResult search(@RequestBody RequestParams params) {
        return hotelService.search(params);
    }

    /**
     * 黑马旅游案例-条件过滤
     * 品牌、城市、星级、价格等过滤功能
     *
     * @param params
     * @return
     */
    @PostMapping("filters")
    public Map<String, List<String>> getFilters(@RequestBody RequestParams params) {
        return hotelService.getFilters(params);
    }

    /**
     * 黑马旅游案例-
     *
     * @param params
     * @return
     */
    @GetMapping("suggestion")
    public List<String> getSuggestion(@RequestParam("key") String key) {
        return hotelService.getSuggestion(key);
    }
}
