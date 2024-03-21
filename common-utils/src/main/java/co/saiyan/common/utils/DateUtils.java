package co.saiyan.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @author larry
 * @createTime 2023/7/6
 * @description DateUtils
 */
public class DateUtils {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_MILLSEC_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String FORMAT_YYYYMM = "yyyyMM";
    public static final String FORMAT_YYYY = "yyyy";

    public enum CycleEnum {

        DAY("day", "日"),
        THREE_DAY("3days", "三日"),
        WEEK("week", "周"),
        MONTH("month", "月"),
        YEAR("year", "年"),
        ;

        private String key;
        private String desc;

        CycleEnum(String key, String desc) {
            this.key = key;
            this.desc = desc;
        }

        public String getKey() {
            return key;
        }

        public String getDesc() {
            return desc;
        }

        public static CycleEnum getByKey(String key) {
            return Arrays.stream(CycleEnum.values()).filter(e -> e.getKey().equals(key)).findFirst().orElse(null);
        }

        public static CycleEnum getByDesc(String desc) {
            return Arrays.stream(CycleEnum.values()).filter(e -> e.getDesc().equalsIgnoreCase(desc)).findFirst()
                    .orElse(null);
        }

        public static CycleEnum getByName(String name) {
            return Arrays.stream(CycleEnum.values()).filter(e -> e.name().equalsIgnoreCase(name)).findFirst()
                    .orElse(null);
        }
    }

    public static final DateTimeFormatter DEFAULT_FORMATER = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
    public static final DateTimeFormatter TIME_FORMATER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    public static final DateTimeFormatter FORMATER_YYYY_MM_DD = DateTimeFormatter.ofPattern(FORMAT_YYYY_MM_DD);
    public static final DateTimeFormatter FORMATER_YYYYMMDD = DateTimeFormatter.ofPattern(FORMAT_YYYYMMDD);
    public static final DateTimeFormatter DEFAULT_MILLSEC_FORMATER = DateTimeFormatter.ofPattern(DEFAULT_MILLSEC_FORMAT);

    public static final long MILLS_SECOND = 1000L;

    public static boolean isDateBetween(Date cur, Date start, Date end) {
        Date startDate = getDateMidnight(start);
        Date endDate = getDateMidnight(end);
        Date curDate = getDateMidnight(cur);
        assert curDate != null;
        assert startDate != null;
        if (curDate.getTime() < startDate.getTime()) return false;
        assert endDate != null;
        return curDate.getTime() <= endDate.getTime();
    }

    public static List<Date> getBetweenDate(Date start, Date end) {
        Date startDate = getDateMidnight(start);
        Date endDate = getDateMidnight(end);

        List<Date> dateList = new ArrayList<>();
        do {
            dateList.add(startDate);
            startDate = addTimes(startDate, 1, TimeUnit.DAY);
        } while (startDate.getTime() <= endDate.getTime());
        return dateList;
    }

    public static Date copyTime(Date fromTime, Date toDate) {
        Date currentStart = getDateMidnight(fromTime);
        long duration = fromTime.getTime() - currentStart.getTime();

        Date changeStart = getDateMidnight(toDate);
        long changeDuration = changeStart.getTime() + duration;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(changeDuration);
        return calendar.getTime();
    }

    /**
     * 当前日期是周几
     *
     * @return 1 ~ 7
     */
    public static int getDayOfWeek() {
        LocalDate today = LocalDate.now();

        return today.getDayOfWeek().getValue();
    }

    /**
     * 获取周的第几天
     *
     * @return
     */
    public static int getDayOfWeek(Date date) {
        LocalDateTime localDateTime = fromUDateToLocalDateTime(date);
        return localDateTime.getDayOfWeek().getValue();

    }

    /**
     * 获取月的第几天
     *
     * @return
     */
    public static int getDayOfMonth(Date date) {
        LocalDateTime localDateTime = fromUDateToLocalDateTime(date);
        return localDateTime.getDayOfMonth();
    }

    private static LocalDateTime fromUDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        FastDateFormat fdf = FastDateFormat.getInstance(pattern);
        return fdf.format(date);
    }

    /**
     * 解析日期
     *
     * @param dateStr
     * @return
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, DEFAULT_FORMAT);
    }

    /**
     * 解析一个日期
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parse(String dateStr, String pattern) {
        FastDateFormat fdf = FastDateFormat.getInstance(pattern);
        try {
            return fdf.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalStateException("parse data error! dateStr" + dateStr + ", pattern:" + pattern, e);
        }
    }

    public static boolean isDate(String dateStr, String pattern) {
        FastDateFormat fdf = FastDateFormat.getInstance(pattern);
        try {
            fdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * java.sql.Timestamp和java.sql.Date虽然都继承java.util.Date,不能用equals来比,要有转换
     */
    public static boolean equals(Date d1, Date d2) {
        return d1 == null ? d2 == null : (d2 == null ? false : d1.getTime() == d2.getTime());
    }

    public static void sleepMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            //ignore
        }
    }


    public static String getStartDate(Date date) {
        return format(date, FORMAT_YYYY_MM_DD) + " 00:00:00";
    }

    public static String getEndDate(Date date) {
        return format(date, FORMAT_YYYY_MM_DD) + " 23:59:59";
    }

    /**
     * 取昨天的日期
     */
    public static String getYesterdayDate(String format) {
        Date d = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), -1);
        return format(d, format);
    }

    /**
     * 获取指定的日期，几天前，几天后
     */
    public static Date calculateDate(int num) {
        return org.apache.commons.lang3.time.DateUtils.addDays(new Date(), num);
    }

    /**
     * 取前N天的日期，默认日期 yyyy-mm-dd
     */
    public static String getPreviousDay(int decreaseDay) {
        return getPreviousDay(decreaseDay, new Date(), FORMAT_YYYY_MM_DD);
    }

    /**
     * 取前N天的日期，默认日期 yyyy-mm-dd
     */
    public static String getPreviousDay(int decreaseDay, Date date) {
        return getPreviousDay(decreaseDay, date, FORMAT_YYYY_MM_DD);
    }

    /**
     * 取几天前的日期
     * 取反值
     */
    public static String getPreviousDay(int decreaseDay, Date date, String format) {
        Date d = org.apache.commons.lang3.time.DateUtils.addDays(date, 0 - decreaseDay);
        return format(d, format);
    }

    /**
     * 取前N天的日期，默认日期 yyyy-mm-dd
     * 按照传入正负数进行计算
     */
    public static String getPreviousDayStr(int decreaseDay, Date date) {
        return getPreviousDay(decreaseDay, date, FORMAT_YYYY_MM_DD);
    }

    /*
     * 获取指定时间秒级别的时间戳
     *
     * @param date 格式：yyyy-MM-dd HH:mm:ss
     * @return secondTime
     */
    public static int getSecondTime(String date) {
        Date startDate = parse(date);
        // 查询时间区间
        return (int) (startDate.getTime() / MILLS_SECOND);
    }

    /**
     * 获取上个月第一天
     */
    public static String getLastMonthFirstDay() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).format(FORMATER_YYYY_MM_DD);
    }

    /**
     * 获取上个月最后一天
     */
    public static String getLastMonthLastDay() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).format(FORMATER_YYYY_MM_DD);
    }

    /**
     * 上一年的第一天
     *
     * @param date
     * @return
     */
    public static String getLastYearFirstDay(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusYears(1).with(
                TemporalAdjusters.firstDayOfYear()).format(FORMATER_YYYY_MM_DD);
    }

    /**
     * 上一年的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastYearLastDay(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusYears(1).with(
                TemporalAdjusters.lastDayOfYear()).format(FORMATER_YYYY_MM_DD);
    }

    /**
     * 上周的这一天
     *
     * @param date
     * @return
     */
    public static Date getLastWeekThisDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, -7);
        return calendar.getTime();
    }

    /**
     * 上周的第一天
     *
     * @param date
     * @return
     */
    public static Date getLastWeekFirstDay(Date date, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, -7);

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }


    /**
     * 上周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastWeekLastDay(Date date, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, -7);

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    /**
     * 获取上一个星期几
     */
    public static String getLastDayOfWeek(DayOfWeek dayOfWeek) {
        return LocalDate.now().with(TemporalAdjusters.previous(dayOfWeek)).format(FORMATER_YYYYMMDD);
    }

    /**
     * 添加分钟
     *
     * @param date
     * @param num
     * @return
     */

    public static Date addMinutes(Date date, int num) {
        return addTimes(date, num, TimeUnit.MINUTE);
    }

    /**
     * 添加一段时间
     *
     * @param date
     * @param num
     * @param timeUnit
     * @return
     */
    public static Date addTimes(Date date, int num, TimeUnit timeUnit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(timeUnit.getCalenderField(), num);
        return cal.getTime();
    }

    /**
     * offset一个月
     *
     * @param date
     * @return
     */
    public static String offsetOneMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_YYYYMMDD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return df.format(calendar.getTime());
    }

    /**
     * offset一年
     *
     * @param date
     * @return
     */
    public static String offsetOneYear(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_YYYYMMDD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        return df.format(calendar.getTime());
    }

    /**
     * 上个月的第一天
     *
     * @param date
     * @return
     */
    public static Date getLastMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 上个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取今天开始时间
     *
     * @return
     */
    public static String getTodayStartTime() {
        return LocalDate.now().format(FORMATER_YYYY_MM_DD) + " 00:00:00";
    }

    /**
     * 获取今天结束时间
     *
     * @return
     */
    public static String getTodayEndTime() {
        return LocalDate.now().format(FORMATER_YYYY_MM_DD) + " 23:59:59";
    }

    /**
     * 周一为第一天
     *
     * @return
     */
    public static Date getThisWeekFirstDay() {
        Calendar calWeek = Calendar.getInstance();
        calWeek.setFirstDayOfWeek(Calendar.MONDAY);
        calWeek.setTime(new Date());
        calWeek.add(Calendar.DATE, 0);
        calWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calWeek.getTime();
    }

    /**
     * 周日为最后一天
     *
     * @return
     */
    public static Date getThisWeekLastDay() {
        Calendar calWeek = Calendar.getInstance();
        calWeek.setFirstDayOfWeek(Calendar.MONDAY);
        calWeek.setTime(new Date());
        calWeek.add(Calendar.DATE, 0);
        calWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calWeek.getTime();
    }

    /**
     * 获取当前月第一天
     */
    public static Date getThisMonthFirstDay() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前月最后一天
     */
    public static Date getThisMonthLastDay() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 今年的第一天
     *
     * @return
     */
    public static Date getThisYearFirstDay() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 今年的最后一天
     *
     * @return
     */
    public static Date getThisYearLastDay() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 这个财年的第一天
     *
     * @param date
     * @return
     */
    public static String getThisFiscalYearFirstDay(Date date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_YYYY);
        SimpleDateFormat df1 = new SimpleDateFormat(FORMAT_YYYYMMDD);
        //年
        String year = df.format(date);
        String fiscalYearStart = year + "0401";
        Date parse = df1.parse(fiscalYearStart);
        if (date.compareTo(parse) >= 0) {
            return fiscalYearStart;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, -1);
            String lastFiscalYear = df.format(calendar.getTime());
            return lastFiscalYear + "0401";
        }
    }

    public static Date getBeginTimeOfFirstDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1, 0, 0, 0); // 将日、时、分、秒设为零
        calendar.set(Calendar.MILLISECOND, 0); // 毫秒设为零
        return calendar.getTime();
    }

    public static Date getEndTimeOfLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0); // 下个月的第一天
        calendar.add(Calendar.SECOND, -1); // 减去1秒，即为当前月的最后一天
        return calendar.getTime();
    }

    public static Date getBeginTimeOfTheYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1, 0, 0, 0); // 将日、时、分、秒设为零
        calendar.set(Calendar.MILLISECOND, 0); // 毫秒设为零
        return calendar.getTime();
    }

    public static Date getEndTimeOfTheYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1, 0, 0, 0); // 下个月的第一天
        calendar.add(Calendar.SECOND, -1); // 减去1秒，即为当前月的最后一天
        return calendar.getTime();
    }

    /**
     * 时间单位
     */
    public enum TimeUnit {
        // 毫秒
        MILLISECOND(Calendar.MILLISECOND),
        // 秒
        SECOND(Calendar.SECOND),
        // 分钟
        MINUTE(Calendar.MINUTE),
        // 小时
        HOUR(Calendar.HOUR_OF_DAY),
        // 天
        DAY(Calendar.DATE),
        // 月
        MONTH(Calendar.MONTH),
        // 年
        YEAR(Calendar.YEAR);

        int calenderField;

        TimeUnit(int calenderField) {
            this.calenderField = calenderField;
        }

        private int getCalenderField() {
            return calenderField;
        }

    }

    /**
     * 获取指定月分
     *
     * @param timeFormat 转换格式
     * @param offset     偏移量 0 当前月  正数 几个月后 负数 几个月前
     * @return
     */
    public static String getAssignMonth(String timeFormat, int offset) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, offset);
        String format = simpleDateFormat.format(c.getTime());
        return format;
    }

    /**
     * 获取指定年份
     *
     * @param timeFormat 转换格式
     * @param offset     偏移量 0 当前年  正数 几年后 负数 几年前
     * @return yyyy
     */
    public static String getAssignYear(String timeFormat, int offset) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, offset);
        String format = simpleDateFormat.format(c.getTime());
        return format;

    }

    public static String getNowTime(String timeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    public static Date now() {
        return new Date();
    }

    public static long getBetweenDay(Date startTime, Date endTime) {

        LocalDateTime from = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime to = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());

        return ChronoUnit.DAYS.between(from, to);
    }

    /**
     * 获取两个指定日期之间的天数
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int getDifference(Date date1, Date date2) {
        if (Objects.isNull(date1) || Objects.isNull(date2)) {
            throw new IllegalArgumentException("date1 or date2 is null");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = sdf.parse(sdf.format(date1));
            date2 = sdf.parse(sdf.format(date2));
        } catch (Exception e) {
            throw new IllegalArgumentException(ExceptionUtils.getRootCauseMessage(e));
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 两个时间是否是同一周期内
     *
     * @param dsStartDate   开始时间
     * @param dsEndDate     结束时间
     * @param format        时间格式
     * @param calendarCycle Calendar的周期类型
     * @return
     */
    public static Boolean isSameDateCycle(String dsStartDate, String dsEndDate, String format, int calendarCycle) {
        if (StringUtils.isEmpty(dsStartDate)
                || StringUtils.isEmpty(dsEndDate)) {
            return false;
        }
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(parse(dsStartDate, format));

        Calendar cEnd = Calendar.getInstance();
        cEnd.setTime(parse(dsEndDate, format));

        //默认 美国以周日为一周的开始 这里改为周一
        if (Calendar.WEEK_OF_YEAR == calendarCycle) {
            cStart.setFirstDayOfWeek(Calendar.MONDAY);
            cEnd.setFirstDayOfWeek(Calendar.MONDAY);
        }
        return cStart.get(calendarCycle) == cEnd.get(calendarCycle);
    }

    /**
     * 获取当月的最后一天的 00:00:00时刻
     *
     * @param date 日期
     * @return 当月的最后一天
     */
    public static Date lastDayOfCurMonth(Date date) {
        if (date == null) {
            return null;
        }

        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);

        return c.getTime();
    }

    /**
     * 获取当前的整点时间
     *
     * @return 当前整点时间
     */
    public static Date currentHourlyTime(Date date) {
        Calendar c = new GregorianCalendar();
        if (Objects.isNull(date)) {
            return null;
        }
        c.setTime(date);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 返回某天的 23：59：59
     */
    public static Date getDateNight(Date date) {
        Calendar c = new GregorianCalendar();
        if (Objects.isNull(date)) {
            return null;
        }
        c.setTime(date);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.HOUR_OF_DAY, 23);
        return c.getTime();
    }

    /**
     * 返回某天的 00:00:00
     */
    public static Date getDateMidnight(Date date) {
        Calendar c = new GregorianCalendar();
        if (Objects.isNull(date)) {
            return null;
        }
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getTodayDate(String pattern) {
        String today = DateFormatUtils.format(System.currentTimeMillis(), pattern);
        Date endDate;
        try {
            endDate = org.apache.commons.lang3.time.DateUtils.parseDate(today, pattern);
        } catch (ParseException e) {
            endDate = new Date();
        }
        return endDate;
    }

    public static Date getEarliestDateByCycle(CycleEnum cycle) {
        Date endDate = DateUtils.getTodayDate(DateUtils.FORMAT_YYYY_MM_DD);
        switch (cycle) {
            case DAY:
                return org.apache.commons.lang3.time.DateUtils.addDays(endDate, -1);
            case THREE_DAY:
                return org.apache.commons.lang3.time.DateUtils.addDays(endDate, -3);
            case WEEK:
                return org.apache.commons.lang3.time.DateUtils.addWeeks(endDate, -1);
            case MONTH:
                return org.apache.commons.lang3.time.DateUtils.addMonths(endDate, -1);
            case YEAR:
                return org.apache.commons.lang3.time.DateUtils.addYears(endDate, -1);
            default:
                return org.apache.commons.lang3.time.DateUtils.addWeeks(endDate, -1);
        }
    }

    public static Date[] resolveMonthStrStartEndDate(String monthStr) {
        Date startTime = null, endTime = null;
        if (StringUtils.isNotBlank(monthStr)) {
            monthStr = StringUtils.replace(monthStr.trim(), "-", StringUtils.EMPTY).substring(0, 6);
            startTime = parse(monthStr, FORMAT_YYYYMM);
            endTime = parse(getEndDate(lastDayOfCurMonth(startTime)));
        }
        Date[] arr = new Date[2];
        arr[0] = startTime;
        arr[1] = endTime;
        return arr;
    }

    public static Date[] getStartAndEndTime(Date date) {
        // get Start And End Time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startTime = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endTime = calendar.getTime();
        Date[] arr = new Date[2];
        arr[0] = startTime;
        arr[1] = endTime;
        return arr;
    }

    public static LocalDateTime stringToLocalDateTime(String timeStr, String dateTimeFormat) {
        return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}
