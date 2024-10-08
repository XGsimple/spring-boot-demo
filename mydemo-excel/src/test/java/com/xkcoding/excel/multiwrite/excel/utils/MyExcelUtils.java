package com.xkcoding.excel.multiwrite.excel.utils;

import com.xkcoding.excel.multiwrite.dto.ExcelSheetReadContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Author XuGang
 * @Date 2024/3/6 18:20
 */
@Slf4j
public class MyExcelUtils {
    public static void readSheets(ExcelSheetReadContext... sheetReadContexts) {
        Arrays.stream(sheetReadContexts).forEach(ExcelSheetReadContext::read);
    }

}