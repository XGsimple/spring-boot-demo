package com.xkcoding.excel.multiwrite.write;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Author XuGang
 * @Date 2024/3/13 15:38
 */
@Slf4j
@Setter
@Getter
@Accessors(chain = true)
public class ExcelWriteTableVO<T> {
    /**
     * 子表对应模型Class
     */
    private Class<T> clazz;

    /**
     * sheet页中子表顺序
     */
    private Integer tableIndex;

    /**
     * 子表间隔行数
     */
    private Integer relativeHeadRowIndex = 1;

    /**
     * 数据
     */
    private List<T> data;

    /**
     * head 参数
     */
    private Map<String, String> vars;
}