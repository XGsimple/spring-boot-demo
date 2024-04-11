package com.xkcoding.bf.handler;

import com.xkcoding.bf.dto.ValidateFailRow;

import java.util.List;

/**
 * 子表数据读处理
 *
 * @Author XuGang
 * @Date 2024/3/15 14:39
 */
@FunctionalInterface
public interface TableDataReadHandler<T> {

    void handle(List<T> data, List<ValidateFailRow<T>> validateFailRows);
}
