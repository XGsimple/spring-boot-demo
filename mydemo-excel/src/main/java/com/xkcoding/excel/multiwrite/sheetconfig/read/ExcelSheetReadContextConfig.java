package com.xkcoding.excel.multiwrite.sheetconfig.read;

import com.xkcoding.excel.multiwrite.dto.ExcelSheetReadContext;
import com.xkcoding.excel.multiwrite.enums.ImportFileTemplateEnum;

/**
 * sheet 模板配置
 *
 * @Author XuGang
 * @Date 2024/3/11 13:39
 */
public interface ExcelSheetReadContextConfig {
    ImportFileTemplateEnum getTemplateEnum();

    Integer getSheetIndex();

    ExcelSheetReadContext configExcelSheetReadContext(byte[] byteArray);
}
