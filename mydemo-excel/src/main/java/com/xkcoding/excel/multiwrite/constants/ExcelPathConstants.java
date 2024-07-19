package com.xkcoding.excel.multiwrite.constants;

/**
 * @Author XuGang
 * @Date 2024/3/15 11:17
 */
public interface ExcelPathConstants {
    /**
     * 通用路径前缀
     */
    String DATA_IMPORT_COMMON_PREFIX_PATH = "/yxt-bigdata-decision/monthly-report/excel";
    /**
     * 导入原文件路径前缀
     */
    String DATA_IMPORT_SOURCE_PREFIX_PATH = DATA_IMPORT_COMMON_PREFIX_PATH + "/source";

    /**
     * 导入错误文件路径前缀
     */
    String DATA_IMPORT_ERROR_PREFIX_PATH = DATA_IMPORT_COMMON_PREFIX_PATH + "/error";
    /**
     * 导入下载模板路径前缀
     */
    String DATA_IMPORT_TEMPLATE_PREFIX_PATH = DATA_IMPORT_COMMON_PREFIX_PATH + "/template";

    /**
     * 月度汇报导入下载模板
     */
    String DATA_IMPORT_MONTH_REPORT_TEMPLATE = DATA_IMPORT_TEMPLATE_PREFIX_PATH + "/月度汇报_template.xlsx";

    /**
     * 测试导入下载模板
     */
    String DATA_IMPORT_TEST_TEMPLATE = DATA_IMPORT_TEMPLATE_PREFIX_PATH + "/SingleSheetMultiTable_template.xlsx";

}