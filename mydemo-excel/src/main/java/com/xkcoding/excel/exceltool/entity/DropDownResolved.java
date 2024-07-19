package com.xkcoding.excel.exceltool.entity;

import com.xkcoding.excel.exceltool.dropdown.annotation.DropDownFields;
import com.xkcoding.excel.exceltool.dropdown.service.IDropDownService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author XuGang
 * @Date 2024/7/19 15:39
 **/
@Slf4j
@Setter
@Getter
public class DropDownResolved {
    /**
     * 下拉内容
     */
    private String[] source;

    /**
     * 设置下拉框的起始行，默认为第二行
     */
    private int firstRow;

    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    private int lastRow;

    public String[] resolveDropDownFields(DropDownFields dropDownFields) {
        if (dropDownFields == null) {
            return null;
        }

        // 获取固定下拉框的内容
        String[] source = dropDownFields.source();
        if (source.length > 0) {
            return source;
        }

        // 获取动态下拉框的内容
        Class<? extends IDropDownService>[] classes = dropDownFields.sourceClass();
        if (classes.length > 0) {
            try {
                IDropDownService excelDynamicSelect = classes[0].newInstance();
                String[] dynamicSelectSource = excelDynamicSelect.getSource();
                if (dynamicSelectSource != null && dynamicSelectSource.length > 0) {
                    return dynamicSelectSource;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("解析动态下拉框数据异常", e);
            }
        }
        return null;
    }

}