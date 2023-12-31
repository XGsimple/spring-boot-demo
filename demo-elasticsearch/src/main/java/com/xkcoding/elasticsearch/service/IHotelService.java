package com.xkcoding.elasticsearch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkcoding.elasticsearch.po.Hotel;
import com.xkcoding.elasticsearch.pojo.PageResult;
import com.xkcoding.elasticsearch.pojo.RequestParams;

import java.util.List;
import java.util.Map;

public interface IHotelService extends IService<Hotel> {
    PageResult search(RequestParams params);

    Map<String, List<String>> getFilters(RequestParams params);

    List<String> getSuggestion(String key);

    void deleteById(Long hotelId);

    void saveById(Long hotelId);
}
