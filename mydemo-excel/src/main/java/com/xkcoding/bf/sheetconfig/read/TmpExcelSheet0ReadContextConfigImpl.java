package com.xkcoding.bf.sheetconfig.read;

import com.xkcoding.bf.dto.ExcelSheetReadContext;
import com.xkcoding.bf.enums.ImportFileTemplateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * excel sheet 配置
 *
 * @Author XuGang
 * @Date 2024/3/11 13:41
 */
@Slf4j
@Component
public class TmpExcelSheet0ReadContextConfigImpl implements ExcelSheetReadContextConfig {
    @Override
    public ImportFileTemplateEnum getTemplateEnum() {
        return ImportFileTemplateEnum.TEST;
    }

    @Override
    public Integer getSheetIndex() {
        return 0;
    }

    @Override
    public ExcelSheetReadContext configExcelSheetReadContext(byte[] byteArray) {
        return null;
    }
}