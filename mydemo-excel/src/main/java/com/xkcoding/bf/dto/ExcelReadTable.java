package com.xkcoding.bf.dto;

/**
 * @Author XuGang
 * @Date 2024/3/7 13:53
 */
public interface ExcelReadTable<T> {
    /**
     * 子表对应模型Class
     */
    Class<T> getClazz();

    /**
     * sheet页号
     */
    Integer getSheetIndex();

    /**
     * sheet名称
     */
    String getSheetName();

    /**
     * 子表名
     */
    String getTableName();

    /**
     * sheet页中顺序
     */
    Integer getTableIndex();

}