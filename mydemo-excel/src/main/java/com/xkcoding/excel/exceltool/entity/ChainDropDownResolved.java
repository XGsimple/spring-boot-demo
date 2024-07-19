package com.xkcoding.excel.exceltool.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 级联下拉框
 */
@AllArgsConstructor
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ChainDropDownResolved {
    //    /**
    //     * 名称
    //     */
    //    private String value;
    //    /**
    //     * 是否有子类
    //     */
    //    private boolean hasChild=true;
    //    /**
    //     * 子类的集合
    //     */
    //    private List<ChainDropDown> subList;
    public static final String ROOT_KEY = "root";
    /**
     * 是否是根目录
     */
    private boolean rootFlag = true;

    private String typeName;

    //    private List<String> data;

    /**
     * 行下标
     */
    private Integer rowIndex = 0;

    private Map<String, List<String>> dataMap = new HashMap<>();

    /**
     * 设置下拉框的起始行，默认为第二行
     */
    private int firstRow;

    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    private int lastRow;

}



