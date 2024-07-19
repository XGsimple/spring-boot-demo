package com.xkcoding.excel.multiwrite.handler;

/**
 * 子表行数据读处理
 *
 * @Author XuGang
 * @Date 2024/3/15 14:39
 */
@FunctionalInterface
public interface TableRowDataReadHandler<T> {

    void handle(T rowData);
}
