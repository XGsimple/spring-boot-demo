package com.xkcoding.excel.exceltool.dropdown.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkcoding.excel.exceltool.dict.entity.DictValue;
import com.xkcoding.excel.exceltool.dict.service.IDictValueService;
import com.xkcoding.excel.exceltool.dropdown.service.IDropDownService;
import com.xkcoding.excel.exceltool.utils.ApplicationContextUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 职业的下拉 实现类
 */
public class OccupationDropDownService implements IDropDownService {

    @Override
    public String[] getSource(String typeValue) {
        // 没法用 @Autowired 注入，所以用ApplicationContext
        IDictValueService dictValueService = ApplicationContextUtils.getBean(IDictValueService.class);
        List<DictValue> list = dictValueService.list(new LambdaQueryWrapper<DictValue>().eq(DictValue::getType,
                                                                                            typeValue));
        if (ObjectUtils.isEmpty(list)) {
            return null;
        }
        Set<String> collect = list.stream().map(DictValue::getValue).collect(Collectors.toSet());
        return collect.toArray(new String[collect.size()]);
    }
}
