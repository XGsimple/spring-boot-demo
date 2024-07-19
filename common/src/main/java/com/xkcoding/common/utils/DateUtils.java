package com.xkcoding.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xkcoding.common.constants.CommonConstants;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @Description: 时间工具类
 * @Author：zyl
 * @Date：2023/11/13 12:46
 */
public class DateUtils {

    //yyyy-MM
    private static final ThreadLocal<DateTimeFormatter> YEAR_MONTH_LOCAL_DATE_FORMAT = ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(
        CommonConstants.DateFormat.YEAR_MONTH_FORMAT));

    //yyyyMM
    private static final ThreadLocal<DateTimeFormatter> YYYYMM_LOCAL_DATE_FORMAT = ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(
        CommonConstants.DateFormat.YYYYMM));

    //yyyy-MM
    public static final ThreadLocal<SimpleDateFormat> YEAR_MONTH_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.YEAR_MONTH_FORMAT));

    //yyyyMM
    public static final ThreadLocal<SimpleDateFormat> YYYYMM_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.YYYYMM));

    //yyyy-MM-dd
    public static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.DATE_FORMAT));

    //yyyy/MM/dd
    public static final ThreadLocal<SimpleDateFormat> YYYYMMDD_SLASH_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.YYYYMMDD_SLASH));

    //yyyy-MM-dd HH
    public static final ThreadLocal<SimpleDateFormat> DATETIME_HOURS_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.DATETIME_HOURS_FORMAT));

    //yyyy-MM-dd HH:mm:ss
    public static final ThreadLocal<SimpleDateFormat> DATETIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.DATETIME_FORMAT));

    //yyyy-MM-dd HH:mm:ss.SSS
    public static final ThreadLocal<SimpleDateFormat> LONG_DATETIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.LONG_DATETIME_FORMAT));

    public static final ThreadLocal<SimpleDateFormat> LONG_COMPACT_DATETIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
        CommonConstants.DateFormat.LONG_COMPACT_DATETIME_FORMAT));

    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final Map<String, ThreadLocal<SimpleDateFormat>> dateFormatMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>() {
        {
            put(CommonConstants.DateFormat.YEAR_MONTH_FORMAT, YEAR_MONTH_FORMAT);
            put(CommonConstants.DateFormat.YYYYMM, YYYYMM_FORMAT);
            put(CommonConstants.DateFormat.DATE_FORMAT, DATE_FORMAT);
            put(CommonConstants.DateFormat.YYYYMMDD_SLASH, YYYYMMDD_SLASH_FORMAT);
            put(CommonConstants.DateFormat.LONG_COMPACT_DATETIME_FORMAT, LONG_COMPACT_DATETIME_FORMAT);
        }
    };

    private static final Map<String, ThreadLocal<DateTimeFormatter>> dateTimeFormatterMap = new HashMap<String, ThreadLocal<DateTimeFormatter>>() {
        {
            put(CommonConstants.DateFormat.YEAR_MONTH_FORMAT, YEAR_MONTH_LOCAL_DATE_FORMAT);
            put(CommonConstants.DateFormat.YYYYMM, YYYYMM_LOCAL_DATE_FORMAT);
        }
    };

    /**
     * 时间戳转换为字符串
     *
     * @param date       时间
     * @param dateFormat 时间格式
     * @return 时间戳
     */
    public static String parse(Long date, String dateFormat) {
        Instant instant = Instant.ofEpochMilli(date);
        LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return time.format(formatter);
    }

    /**
     * 判断是否是每个月开始的前几天
     *
     * @param date
     * @return
     */
    public static boolean isEveryDayFirstNMonth(Date date, Integer day) {
        LocalDateTime dateTime = date2LocalDateTime(date);
        // 获取当前时间距离小时开始时间的分钟数
        int minutesSinceStartOfHour = dateTime.getDayOfMonth();
        return minutesSinceStartOfHour < day;
    }

    /**
     * 判断是否是每个小时开始的前几分钟
     *
     * @param date
     * @return
     */
    public static boolean isEveryHourFirstNMinutes(Date date, Integer minute) {
        LocalDateTime dateTime = date2LocalDateTime(date);
        // 获取当前时间距离小时开始时间的分钟数
        int minutesSinceStartOfHour = dateTime.getMinute() + (dateTime.getSecond() / 60);
        return minutesSinceStartOfHour < minute;
    }

    /**
     * 判断是否是每个天的前N个小时
     *
     * @param date
     * @return
     */
    public static boolean isEveryDayFirstNHours(Date date, Integer hour) {
        LocalDateTime dateTime = date2LocalDateTime(date);
        // 获取当前时间距离小时开始时间的分钟数
        int hoursSinceStartOfDay = dateTime.getHour() + (dateTime.getMinute() / 60);
        return hoursSinceStartOfDay < hour;
    }

    /**
     * 传入开始月和结束月,计算之间天数
     * 传入的参数格式为 yyyy-MM
     * 返回的参数格式为 yyyyMM
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static Long monthsCountingDays(String startTime, String endTime) {
        Date start = DateUtil.parse(startTime, "yyyy-MM");
        Date end = DateUtil.parse(endTime, "yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        end = calendar.getTime();
        return ChronoUnit.DAYS.between(date2LocalDateTime(start), date2LocalDateTime(end)) + 1;
    }

    /**
     * 传入开始月和结束月，返回其环比开始月和环比结束月
     * 传入的参数格式为 yyyy-MM
     * 返回的参数格式为 yyyyMM
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static List<String> getLastMonthsStr(String startTime, String endTime) {
        DateTime start = DateUtil.parse(startTime, "yyyy-MM");
        DateTime end = DateUtil.parse(endTime, "yyyy-MM");
        //相差数
        long daysBetween = ChronoUnit.MONTHS.between(date2LocalDateTime(start), date2LocalDateTime(end));
        Date lastStartTime = dateAddMonths(start, (int)-daysBetween - 1);
        Date lastEndTime = dateAddMonths(end, (int)-daysBetween - 1);
        List<String> dateList = new ArrayList<>();
        dateList.add(DateUtil.format(lastStartTime, "yyyyMM"));
        dateList.add(DateUtil.format(lastEndTime, "yyyyMM"));
        return dateList;
    }

    /**
     * 字符串时间转换为时间戳
     *
     * @param date       时间
     * @param dateFormat 时间格式
     * @return 时间戳
     */
    public static Long parse(String date, String dateFormat) {
        return Optional.ofNullable(date)
                       .map(d -> DateUtil.parse(date, dateFormat).toInstant().toEpochMilli())
                       .orElse(null);
    }

    /**
     * yyyy-MM-dd 字符串时间转换为时间戳
     *
     * @param date 时间
     * @return 时间戳
     */
    public static Long parseyyyyDDmmDate(String date) {
        return parse(date, CommonConstants.DateFormat.DATE_FORMAT);
    }

    /**
     * yyyy-MM-dd HH 字符串时间转换为时间戳
     *
     * @param date 时间
     * @return 时间戳
     */
    public static Long parseyyyyDDmmHHDate(String date) {
        return parse(date, CommonConstants.DateFormat.DATETIME_HOURS_FORMAT);
    }

    /**
     * 传入开始时间和结束时间，返回其同比开始时间和同比结束时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static List<String> getYearTimeStr(String startTime, String endTime) {
        DateTime start = DateUtil.parse(startTime, "yyyy-MM");
        DateTime end = DateUtil.parse(endTime, "yyyy-MM");
        List<Date> getYearTime = getYearTime(start, end);
        List<String> dateList = new ArrayList<>();
        dateList.add(DateUtil.format(getYearTime.get(0), "yyyyMM"));
        dateList.add(DateUtil.format(getYearTime.get(1), "yyyyMM"));
        return dateList;
    }

    /**
     * 同比时间获取
     *
     * @param startTimeStr
     * @param endTimeStr
     * @param oldDatePattern
     * @param newDatePattern
     * @return
     */
    public static List<String> getYoyDateStr(String startTimeStr, String endTimeStr, String oldDatePattern,
                                             String newDatePattern) {
        Date startTime = DateUtil.parse(startTimeStr, oldDatePattern);
        Date endTime = DateUtil.parse(endTimeStr, oldDatePattern);
        Date yoyStartTime = DateUtils.dateAddYear(startTime, -1);
        Date yoyEndTime = DateUtils.dateAddYear(endTime, -1);
        return Arrays.asList(DateUtil.format(yoyStartTime, newDatePattern),
                             DateUtil.format(yoyEndTime, newDatePattern));
    }

    /**
     * 同比时间获取
     *
     * @param startTimeStr
     * @param endTimeStr
     * @param datePattern
     * @return
     */
    public static List<String> getYoyDateStr(String startTimeStr, String endTimeStr, String datePattern) {
        return getYoyDateStr(startTimeStr, endTimeStr, datePattern, datePattern);
    }

    /**
     * 环比时间获取
     *
     * @param startTimeStr
     * @param endTimeStr
     * @param oldDatePattern
     * @param newDatePattern
     * @return
     */
    public static List<String> getDodDateStr(String startTimeStr, String endTimeStr, String oldDatePattern,
                                             String newDatePattern) {
        if (CommonConstants.DateFormat.YEAR_MONTH_FORMAT.equals(oldDatePattern)) {
            Date endTime = DateUtil.parse(endTimeStr, oldDatePattern);
            LocalDate dodMonthDate = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusMonths(1);
            LocalDate dodStartOfMonth = dodMonthDate.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate dodEndOfMonth = dodMonthDate.with(TemporalAdjusters.lastDayOfMonth());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(newDatePattern);
            return Arrays.asList(dateTimeFormatter.format(dodStartOfMonth), dateTimeFormatter.format(dodEndOfMonth));
        } else {
            DateTime start = DateUtil.parse(startTimeStr, oldDatePattern);
            DateTime end = DateUtil.parse(endTimeStr, oldDatePattern);
            List<Date> getLastTime = getLastTime(start, end);
            List<String> dodDates = new ArrayList<>();
            dodDates.add(DateUtil.format(getLastTime.get(0), newDatePattern));
            dodDates.add(DateUtil.format(getLastTime.get(1), newDatePattern));
            return dodDates;
        }
    }

    /**
     * 环比时间获取
     *
     * @param startTimeStr
     * @param endTimeStr
     * @param datePattern
     * @return
     */
    public static List<String> getDodDateStr(String startTimeStr, String endTimeStr, String datePattern) {
        return getDodDateStr(startTimeStr, endTimeStr, datePattern, datePattern);
    }

    /**
     * 月份的环比时间获取
     *
     * @param selectTimeStr
     * @param datePattern
     * @return
     */
    public static List<String> getDodMonthDateStr(String selectTimeStr, String datePattern) {
        return getDodDateStr(selectTimeStr,
                             selectTimeStr,
                             CommonConstants.DateFormat.YEAR_MONTH_FORMAT,
                             CommonConstants.DateFormat.DATE_FORMAT);
    }

    /**
     * 传入开始时间和结束时间，返回其同比开始时间和同比结束时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static List<Date> getYearTime(Date startTime, Date endTime) {
        Date lastStartTime = dateAddYear(startTime, -1);
        Date lastEndTime = dateAddYear(endTime, -1);
        List<Date> dateList = new ArrayList<>();
        dateList.add(lastStartTime);
        dateList.add(lastEndTime);
        return dateList;
    }

    /**
     * 传入开始时间和结束时间，返回其环比开始时间和环比结束时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static List<String> getLastTimeStr(String startTime, String endTime) {
        DateTime start = DateUtil.parse(startTime, "yyyy-MM");
        DateTime end = DateUtil.parse(endTime, "yyyy-MM");
        List<Date> getLastTime = getLastTime(start, end);
        List<String> dateList = new ArrayList<>();
        dateList.add(DateUtil.format(getLastTime.get(0), "yyyyMM"));
        dateList.add(DateUtil.format(getLastTime.get(1), "yyyyMM"));
        return dateList;
    }

    /**
     * 传入开始时间和结束时间，返回其环比开始时间和环比结束时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static List<Date> getLastTime(Date startTime, Date endTime) {
        LocalDateTime start = date2LocalDateTime(startTime);
        LocalDateTime end = date2LocalDateTime(endTime);
        //相差天数
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        Date lastStartTime = dateAddDay(startTime, (int)-daysBetween - 1);
        Date lastEndTime = dateAddDay(endTime, (int)-daysBetween - 1);
        List<Date> dateList = new ArrayList<>();
        dateList.add(lastStartTime);
        dateList.add(lastEndTime);
        return dateList;
    }

    /**
     * 传入"yyyy-MM" 的月获取当前月开始和结束时间
     * 例如：2023-08
     *
     * @param monthStr 月
     * @return
     */
    public static List<Date> getStartEndTime(String monthStr) {
        LocalDate time = LocalDate.from(date2LocalDateTime(DateUtil.parse(monthStr, "yyyy-MM")));
        LocalDate startOfMonth = time.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = time.with(TemporalAdjusters.lastDayOfMonth());
        List<Date> dateList = new ArrayList<>();
        dateList.add(localDateTime2Date(startOfMonth.atStartOfDay()));
        dateList.add(localDateTime2Date(endOfMonth.atStartOfDay()));
        return dateList;
    }

    /**
     * 添加年数
     *
     * @param dateStr 时间
     * @param year    加年天
     * @return
     */
    public static String dateAddYearStr(String dateStr, int year) {
        Date date = dateAddYear(DateUtil.parse(dateStr, "yyyy-MM"), year);
        return DateUtil.format(date, "yyyyMM");
    }

    /**
     * 添加年数
     *
     * @param date 时间
     * @param year 加年天
     * @return
     */
    public static Date dateAddYear(Date date, int year) {
        LocalDateTime dateTime = date2LocalDateTime(date);
        dateTime = dateTime.plusYears(year);
        return localDateTime2Date(dateTime);
    }

    /**
     * 添加天数
     *
     * @param date 时间
     * @param day  加几天
     * @return
     */
    public static Date dateAddDay(Date date, int day) {
        LocalDateTime dateTime = date2LocalDateTime(date);
        dateTime = dateTime.plusDays(day);
        return localDateTime2Date(dateTime);
    }

    /**
     * 添加小时
     *
     * @param date 时间
     * @param hour 加几小时
     * @return
     */
    public static Date dateAddHour(Date date, int hour) {
        LocalDateTime dateTime = date2LocalDateTime(date);
        dateTime = dateTime.plusHours(hour);
        return localDateTime2Date(dateTime);
    }

    /**
     * 添加月
     *
     * @param dateStr 时间
     * @param months  加几个月
     * @return
     */

    public static String dateAddMonthsStr(String dateStr, int months) {
        Date date = dateAddMonths(DateUtil.parse(dateStr, "yyyy-MM"), months);
        return DateUtil.format(date, "yyyyMM");
    }

    /**
     * 添加月
     *
     * @param date   时间
     * @param months 加几个月
     * @return
     */
    public static Date dateAddMonths(Date date, int months) {
        LocalDateTime dateTime = date2LocalDateTime(date);
        dateTime = dateTime.plusMonths(months);
        return localDateTime2Date(dateTime);
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime 时间
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 当前日期时间戳
     *
     * @return
     */
    public static Long nowDayTimestamp() {
        String date = DateUtil.formatDate(new Date());
        return DateUtils.parseyyyyDDmmDate(date);
    }

    /**
     * 昨天日期时间戳
     *
     * @return
     */
    public static Long yesterdayTimestamp() {
        String date = DateUtil.formatDate(DateUtils.dateAddDay(new Date(), -1));
        return DateUtils.parseyyyyDDmmDate(date);
    }

    /**
     * 当前日期
     *
     * @return
     */
    public static String nowDayString() {
        return DateUtil.formatDate(new Date());
    }

    /**
     * 昨天日期
     *
     * @return
     */
    public static String yesterdayString() {
        return DateUtil.formatDate(DateUtils.dateAddDay(new Date(), -1));
    }

    /**
     * 获取指定日期的前nums个月
     * 如果选择日期，是当月，则向前推一个月
     *
     * @param selectDateStr
     * @param oldPattern
     * @param nums
     * @param newPattern
     * @return
     */
    public static List<String> getPreMonthsStr(String selectDateStr, String oldPattern, Integer nums,
                                               String newPattern) {
        DateTime selectDate = DateUtil.parse(selectDateStr, oldPattern);
        selectDate.setMutable(false);
        DateTime now = DateTime.now();
        int nowMonth = now.month();
        List<String> months = new ArrayList<>(nums);
        int selectDateMonth = selectDate.month();
        int start = nowMonth == selectDateMonth ? 1 : 0;
        for (int i = start; i < nums + start; i++) {
            months.add(DateUtil.format(selectDate.offset(DateField.MONTH, -i), newPattern));
        }
        return months;
    }

    /**
     * 获取指定日期的前nums个月
     * 如果选择日期，是当月，则向前推一个月
     *
     * @param selectDateStr
     * @param nums
     * @param pattern
     * @return
     */
    public static List<String> getPreMonthsStr(String selectDateStr, Integer nums, String pattern) {
        return getPreMonthsStr(selectDateStr, pattern, nums, pattern);
    }

    /**
     * 计算两个日期之间相差多少日期
     * fomat:yyyy-MM
     *
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static int betweenMonthNum(String startTime, String endTime, String format) {
        return (int)DateUtil.betweenMonth(DateUtil.parse(startTime, format), DateUtil.parse(endTime, format), false);
    }

    /**
     * 获取前几月
     *
     * @param date
     * @param num
     * @return
     */
    public static List<String> getLastMonth(Date date, int num, String format) {
        List<String> months = new ArrayList<>();
        for (LocalDateTime nearestMonth : getNearestMonths(date2LocalDateTime(date), num)) {
            months.add(DateUtil.format(localDateTime2Date(nearestMonth), format));
        }
        return months;
    }

    /**
     * 获取前几年
     *
     * @param date
     * @param num
     * @return
     */
    public static List<String> getLastYear(Date date, int num) {
        List<String> years = new ArrayList<>();
        String curYear = DateUtil.format(date, "yyyy");
        Integer curYearInt = Integer.parseInt(curYear);
        for (int i = 0; i < num; ++i) {
            years.add(String.valueOf(curYearInt - i));
        }
        return years;
    }

    public static List<LocalDateTime> getNearestMonths(LocalDateTime date, int months) {
        List<LocalDateTime> result = new ArrayList<>(months);
        for (int i = 0; i < months; i++) {
            result.add(date);
            date = date.plusMonths(-1);
        }
        return result;
    }

    /**
     * 获取自然月的环比开始日期和结束日期
     *
     * @param monthStr
     * @return
     */
    public static List<Date> getLastMonthStartEndTime(String monthStr) {
        Date time = dateAddMonths(DateUtil.parse(monthStr, "yyyy-MM"), -1);
        return getStartEndTime(DateUtil.format(time, "yyyy-MM"));
    }

    /**
     * 判断两个日期是否是整月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean checkTwoDayIsMonth(Date startDate, Date endDate) {
        return startDate.getDate() == 1 && DateUtil.isLastDayOfMonth(endDate);
    }

    /**
     * 传入String的date,返回新格式的String类型date
     *
     * @param dateStr
     * @param oldFormat
     * @param newFormat
     * @return
     */
    public static String strDateFormat(String dateStr, String oldFormat, String newFormat) {
        Conditions.assertTrue(dateFormatMap.containsKey(newFormat), "newFormat is not supported");
        ThreadLocal<SimpleDateFormat> newDateFormat = dateFormatMap.get(newFormat);
        DateTime parsed = DateUtil.parse(dateStr, oldFormat);
        return newDateFormat.get().format(parsed);
    }

    /**
     * 获取指定时间的第一个月，形如newFormatStr
     *
     * @return
     */
    public static String getFirstMonthOfTheYear(String yearMonth, String newFormatStr) {
        Conditions.assertTrue(dateFormatMap.containsKey(newFormatStr), "newFormat is not supported");
        Conditions.assertTrue(!StringUtils.isBlank(yearMonth), "yearMonth is blank");
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterMap.get(newFormatStr).get();
        String[] yearMonthArr = yearMonth.split("-");
        int year = Integer.parseInt(yearMonthArr[0]);
        int month = Integer.parseInt(yearMonthArr[1]);
        LocalDate yearMonthDate = LocalDate.of(year, month, 1);
        LocalDate firstDayOfMonth = yearMonthDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate firstMonthOfYear = firstDayOfMonth.withMonth(1);
        return firstMonthOfYear.format(dateTimeFormatter);
    }

    /**
     * 获取形如2024.Q1的年季度，返回季度的首末月份
     *
     * @param yearQuarterStr 年季度，2024.Q1
     * @return 季度的首末月份
     */
    public static Tuple2<String, String> getFirstAndLastMonthOfQuarter(String yearQuarterStr, String newFormatStr) {
        Conditions.assertTrue(dateFormatMap.containsKey(newFormatStr), "newFormat is not supported");
        Conditions.assertTrue(!StringUtils.isBlank(yearQuarterStr), "yearQuarterStr is blank");
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterMap.get(newFormatStr).get();
        String[] yearQuarterArr = yearQuarterStr.split("\\.");
        int year = Integer.parseInt(yearQuarterArr[0]);
        int quarter = Integer.parseInt(yearQuarterArr[1].replaceFirst("Q", ""));
        Month firstMonthOfQuarter = Month.of((quarter - 1) * 3 + 1);
        Month lastMonthOfQuarter = firstMonthOfQuarter.plus(2L);
        LocalDate firstDayOfQuarter = LocalDate.of(year, firstMonthOfQuarter, 1);
        LocalDate lastDayOfQuarter = LocalDate.of(year, lastMonthOfQuarter, 1);
        String formattedFirstMonthOfQuarter = firstDayOfQuarter.format(dateTimeFormatter);
        String formattedLastMonthOfQuarter = lastDayOfQuarter.format(dateTimeFormatter);
        return Tuple.of(formattedFirstMonthOfQuarter, formattedLastMonthOfQuarter);
    }

    /**
     * 根据要求的格式，格式化时间，返回String
     *
     * @param format 默认：yyyy-MM-dd HH:mm:ss.SSS
     * @param time   要格式化的时间
     * @return 时间字符串
     */
    public static String toStr(String format, Date time) {
        SimpleDateFormat df = null;
        try {
            Optional<ThreadLocal<SimpleDateFormat>> optional = Optional.ofNullable(dateFormatMap.get(format));
            if (optional.isPresent()) {
                df = optional.get().get();
            } else {
                df = new SimpleDateFormat(CommonConstants.DateFormat.LONG_DATETIME_FORMAT);
            }
            return df.format(time);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转时间
     *
     * @param source yyyy-MM-dd HH:mm:ss.SSS 格式的字符串
     * @return
     */
    public static Date toDate(String source) {
        String formatString = "yyyy-MM-dd hh:mm:ss";
        if (source == null || "".equals(source.trim())) {
            return null;
        }
        source = source.trim();
        if (source.matches("^\\d{4}$")) {
            formatString = "yyyy";
        } else if (source.matches("^\\d{4}-\\d{1,2}$")) {
            formatString = "yyyy-MM";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            formatString = "yyyy-MM-dd";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}$")) {
            formatString = "yyyy-MM-dd hh";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            formatString = "yyyy-MM-dd hh:mm";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            formatString = "yyyy-MM-dd hh:mm:ss";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$")) {
            formatString = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            Date date = sdf.parse(source);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得本小时的开始时间
     *
     * @return
     */
    public static Date getHourStartTime(Date date) {
        Date dt = null;
        try {
            dt = DATETIME_HOURS_FORMAT.get().parse(DATETIME_HOURS_FORMAT.get().format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本小时的结束时间
     *
     * @return
     */
    public static Date getHourEndTime(Date date) {
        Date dt = null;
        try {
            dt = LONG_DATETIME_FORMAT.get().parse(DATETIME_HOURS_FORMAT.get().format(date) + ":59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本天的开始时间
     *
     * @return
     */
    public static Date getDayStartTime(Date date) {
        Date dt = null;
        try {
            dt = DATE_FORMAT.get().parse(DATE_FORMAT.get().format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本天的结束时间
     *
     * @return
     */
    public static Date getDayEndTime(Date date) {
        Date dt = null;
        try {
            dt = LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(date) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前时间是星期几
     *
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.DAY_OF_WEEK);
        return week_of_year - 1;
    }

    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public static Date getWeekStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 00:00:00.000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public static Date getWeekEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 23:59:59.999"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本月的开始时间
     *
     * @return
     */
    public static Date getMonthStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.DATE, 1);
            dt = DATE_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 本月的结束时间
     *
     * @return
     */
    public static Date getMonthEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            dt = LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前年的开始时间
     *
     * @return
     */
    public static Date getYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            dt = DATE_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前年的结束时间
     *
     * @return
     */
    public static Date getYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            dt = LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的开始时间
     *
     * @return
     */
    public static Date getQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 3);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 6);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 9);
            }
            c.set(Calendar.DATE, 1);
            dt = LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 00:00:00.000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static Date getQuarterEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取前/后半年的开始时间
     *
     * @return
     */
    public static Date getHalfYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 6);
            }
            c.set(Calendar.DATE, 1);
            dt = LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 00:00:00.000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;

    }

    /**
     * 获取前/后半年的结束时间
     *
     * @return
     */
    public static Date getHalfYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = LONG_DATETIME_FORMAT.get().parse(DATE_FORMAT.get().format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取月旬 三旬: 上旬1-10日 中旬11-20日 下旬21-31日
     *
     * @param date
     * @return
     */
    public static int getTenDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        if (i < 11) {
            return 1;
        } else if (i < 21) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 获取所属旬开始时间
     *
     * @param date
     * @return
     */
    public static Date getTenDayStartTime(Date date) {
        int ten = getTenDay(date);
        try {
            if (ten == 1) {
                return getMonthStartTime(date);
            } else if (ten == 2) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-11");
                return DATE_FORMAT.get().parse(df.format(date));
            } else {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-21");
                return DATE_FORMAT.get().parse(df.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取所属旬结束时间
     *
     * @param date
     * @return
     */
    public static Date getTenDayEndTime(Date date) {
        int ten = getTenDay(date);
        try {
            if (ten == 1) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-10 23:59:59.999");
                return LONG_DATETIME_FORMAT.get().parse(df.format(date));
            } else if (ten == 2) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-20 23:59:59.999");
                return LONG_DATETIME_FORMAT.get().parse(df.format(date));
            } else {
                return getMonthEndTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 属于本年第几天
     *
     * @return
     */
    public static int getYearDayIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 属于本年第几周
     *
     * @return
     */
    public static int getYearWeekIndex(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 属于本年第几月
     *
     * @return
     */
    public static int getYearMonthIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 当前属于本年第几个季度
     *
     * @return
     */
    public static int getYeartQuarterIndex(Date date) {
        int month = getYearMonthIndex(date);
        if (month <= 3) {
            return 1;
        } else if (month <= 6) {
            return 2;
        } else if (month <= 9) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * 获取date所属年的所有天列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {
            Date st = getDayStartTime(calendar.getTime());
            Date et = getDayEndTime(calendar.getTime());
            result.add(new Date[] {st, et});
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;

    }

    /**
     * 获取date所属年的所有星期列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearWeekList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        while (calendar.getTime().before(endtm)) {
            Date st = getWeekStartTime(calendar.getTime());
            Date et = getWeekEndTime(calendar.getTime());
            result.add(new Date[] {st, et});
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return result;

    }

    /**
     * 获取date所属年的所有月列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearMonthList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        while (calendar.getTime().before(endtm)) {
            Date tm = calendar.getTime();
            Date st = getMonthStartTime(tm);
            Date et = getMonthEndTime(tm);
            result.add(new Date[] {st, et});
            calendar.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 获取date所属年的所有季度列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearQuarterList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        while (calendar.getTime().before(endtm)) {
            Date st = getQuarterStartTime(calendar.getTime());
            Date et = getQuarterEndTime(calendar.getTime());
            result.add(new Date[] {st, et});
            calendar.add(Calendar.MONTH, 3);
        }
        return result;
    }

    /**
     * 获取date所属月份的所有旬列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> monthTenDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getMonthStartTime(date);
        Date endtm = getMonthEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {
            Date st = getTenDayStartTime(calendar.getTime());
            Date et = getTenDayEndTime(calendar.getTime());
            result.add(new Date[] {st, et});
            calendar.add(Calendar.DAY_OF_MONTH, 11);
        }
        return result;
    }

    /**
     * 获取date所属年的所有月旬列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearTenDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {//
            result.addAll(monthTenDayList(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 测试：获取当年所有日期列表
     */
    private static void testYearDayList() {
        List<Date[]> datas = yearDayList(new Date());
        for (int i = 0; i < datas.size(); i++) {
            Date[] date = datas.get(i);
            System.out.println("（一年的日期列表）第" + (i + 1) + "天：" + DATETIME_FORMAT.get().format(date[0]) + " " +
                               DATETIME_FORMAT.get().format(date[1]));
        }
    }

    /**
     * 测试：获取当年所有星期列表
     */
    private static void testYearWeekList() {
        List<Date[]> datas = yearWeekList(new Date());
        for (int i = 0; i < datas.size(); i++) {
            Date[] date = datas.get(i);
            System.out.println("（一年的周列表）第" + (i + 1) + "周：" + DATETIME_FORMAT.get().format(date[0]) + " " +
                               DATETIME_FORMAT.get().format(date[1]));
        }
    }

    /**
     * 测试：获取当年所有季度列表
     */
    private static void testYearQuarterList() {
        List<Date[]> datas = yearQuarterList(new Date());
        for (int i = 0; i < datas.size(); i++) {
            Date[] date = datas.get(i);
            System.out.println("（一年的季度列表）第" + (i + 1) + "季度：" + DATETIME_FORMAT.get().format(date[0]) + " " +
                               DATETIME_FORMAT.get().format(date[1]));
        }
    }

    /**
     * 测试：获取当年所有月份列表
     */
    private static void testYearMonthList() {
        List<Date[]> datas = yearMonthList(new Date());
        for (int i = 0; i < datas.size(); i++) {
            Date[] date = datas.get(i);
            System.out.println("（一年的月列表）第" + (i + 1) + "月：" + DATETIME_FORMAT.get().format(date[0]) + " " +
                               DATETIME_FORMAT.get().format(date[1]));
        }
    }

    /**
     * 测试：获取当月所有旬列表
     */
    private static void testMonthTenDayList() {
        //Date no= DateTimeTools.toDateTime("2018-02-01 15:38:15");
        List<Date[]> datas = monthTenDayList(new Date());
        for (int i = 0; i < datas.size(); i++) {
            Date[] date = datas.get(i);
            System.out.println("(一月的旬列表)第" + (i % 3 + 1) + "旬：" + DATETIME_FORMAT.get().format(date[0]) + " " +
                               DATETIME_FORMAT.get().format(date[1]));
        }
    }

    /**
     * 测试：获取当年所有旬列表
     */
    private static void testyearTenDayList() {
        List<Date[]> datas = yearTenDayList(new Date());
        for (int i = 0; i < datas.size(); i++) {
            Date[] date = datas.get(i);
            System.out.println(
                "（一年的旬列表）第" + (i / 3 + 1) + "月" + (i % 3 + 1) + "旬：" + DATETIME_FORMAT.get().format(date[0]) +
                " " + DATETIME_FORMAT.get().format(date[1]));
        }
    }
}
