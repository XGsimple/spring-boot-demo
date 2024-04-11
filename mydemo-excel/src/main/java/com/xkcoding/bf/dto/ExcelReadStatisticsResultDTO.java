package com.xkcoding.bf.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据统计结果 DTO
 **/
@Getter
@Setter
@ToString(callSuper = true)
public class ExcelReadStatisticsResultDTO {
    private static final String IMPORT_FILE_VALIDATE_DETAIL_FORMAT = "总计%d条数据，校验成功%d条，校验失败%d条！";
    /**
     * 总行数
     */
    private Integer totalRows = 0;

    /**
     * 校验有错误的总行数
     */
    private Integer totalValidateFailRows = 0;

    /**
     * 是否校验失败
     */
    private Boolean bHasValidateError = Boolean.FALSE;

    public String getImportFileDetail() {
        return String.format(IMPORT_FILE_VALIDATE_DETAIL_FORMAT,
                             totalRows,
                             totalRows - totalValidateFailRows,
                             totalValidateFailRows);
    }

    public Boolean hasValidateError() {
        return bHasValidateError;
    }

}
