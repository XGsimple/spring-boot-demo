package com.xkcoding.bf.constants;

/**
 * 通用常量
 *
 * @Author XuGang
 * @Date 2023/11/3 14:19
 */
public interface CommonConstants {
    /**
     * 水印
     */
    interface WaterMark {
        //默认水印
        String defaultTemple = "由大数据产研部提供数据服务";
    }

    /**
     * 日期格式化
     */
    interface DateFormat {
        String YEAR_MONTH_FORMAT = "yyyy-MM";
        String DATE_FORMAT = "yyyy-MM-dd";
        String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        String LONG_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        String DATETIME_MINUTES_FORMAT = "yyyy-MM-dd HH:mm";
        String DATETIME_HOURS_FORMAT = "yyyy-MM-dd HH";
        String DATE_FORMAT_ZH = "yyyy年MM月dd日";
        String DATETIME_FORMAT_ZH = "yyyy年MM月dd日 HH时:mm分:ss秒";
        String DATETIME_MINUTES_FORMAT_ZH = "yyyy年MM月dd日 HH时:mm分";
        String DATETIME_HOURS_FORMAT_ZH = "yyyy年MM月dd日 HH时";
        String DATETIME_MONTH_FORMAT_ZH = "yyyy年MM月";
        String YYYYMMDD = "yyyyMMdd";
        String YYYYMMDD_SLASH = "yyyy/MM/dd";
        String YYYY = "yyyy";
        String YYYYMM = "yyyyMM";
        String YYYY_MM = "yyyy.MM";

        String YYYYMMDDHH = "yyyyMMddHH";
        String LONG_COMPACT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    }
}
