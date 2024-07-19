package com.xkcoding.excel.multiwrite.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 错误文件 DTO
 **/
@Getter
@Builder
@ToString(callSuper = true)
public class ExportErrorFileResultDTO {

    /**
     * 反馈文件key
     */
    private String key;

    /**
     * 反馈文件名称
     */
    private String name;

}
