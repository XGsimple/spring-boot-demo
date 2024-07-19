package com.xkcoding.excel.multiwrite.dto;

import com.xkcoding.excel.multiwrite.enums.ImportStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 导入文件结果 DTO
 **/
@Getter
@Setter
@Builder
public class ImportFileResultDTO {

    /**
     * 操作状态
     */
    private ImportStatus importStatus;

    /**
     * 反馈文件名
     */
    private String feedbackFileName;

    /**
     * 反馈文件
     */
    private String feedbackFileKey;

    /**
     * 详情
     */
    private String detail;

    /**
     * 文件字节数组
     */
    private byte[] byteArray;

    @Override
    public String toString() {
        return "ImportFileResultDTO{" + "importStatus=" + importStatus + ", feedbackFileName='" + feedbackFileName +
               '\'' + ", feedbackFileKey='" + feedbackFileKey + ", detail='" + detail + '\'' + '}';
    }
}
