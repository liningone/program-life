package org.programlife.investment.stock.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DEFAULT_SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    public static long parseMills(String date) {
        if (date.length() == 10){
            date = completeTime(date);
        }
        Timestamp ts = Timestamp.valueOf(date);
        return ts.getTime();
    }

    public static int getYear(long timeMills) {
        Date date = new Date(timeMills);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getYear(String dateStr) {
        Date date = parseDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(String dateStr) {
        Date date = parseDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    public static int getDayOfMonth(String dateStr) {
        if (dateStr.length() == 10){
            dateStr = completeTime(dateStr);
        }
        Date date = parseDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /*
    输入：时间撮
    输出：时间字符串（默认格式）
     */
    public static String parseDateStr(long timeMills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = new Date(timeMills);
        return simpleDateFormat.format(date);
    }

    /*
    输入：Date对象
    输出：时间字符串（默认格式）
    */
    public static String parseDateStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String parseDateStr(long timeMills, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(timeMills);
        return simpleDateFormat.format(date);
    }

    public static String parseDateStr(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String dateString = date.format(formatter);
        return dateString;
    }

    public static String parseDateStr(String originalDateStr, String targetFormat) {
        DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern(targetFormat);
        LocalDateTime originalDateTime = LocalDateTime.parse(originalDateStr, originalFormatter);
        String targetDateTimeString = originalDateTime.format(targetFormatter);
        return targetDateTimeString;
    }

    public static String parseDateStr(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_SIMPLE_DATE_FORMAT);
        return date.format(formatter);
    }

    public static Date parseDate(String dateStr) {
        //dateStr likes: "2020-02-13 16:01:30";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static LocalDate parseLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_SIMPLE_DATE_FORMAT);

        // 解析时间字符串为LocalDate对象
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return date;
    }

    public static LocalDate parseLocalDate(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        // 解析时间字符串为LocalDate对象
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return date;
    }


    public static String getCurrentTime() {
        return parseDateStr(new Date());
    }

    //2023-05-12 -> 2023-05-12 00:00:00
    public static String completeTime(String date) {
        if (date.length() == 10){
            return date + " 00:00:00";
        }
        return null;
    }

    public static int getDayOfWeek(String dateStr) {
        if (dateStr.length() == 10){
            dateStr = completeTime(dateStr);
        }
        Date date = parseDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int res = c.get(Calendar.DAY_OF_WEEK);

        //把星期一作为第一天，星期天为最后一天
        res --;
        if (res == 0) {
            res = 7;
        }
        return res;
    }

    public static long plusMonths(long originMills, int monthNum) {
        Instant instant = Instant.ofEpochMilli(originMills);
        // 将Instant对象转换为LocalDate对象
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate nextMonthDate = localDate.plusMonths(monthNum);
        return nextMonthDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static void main(String[] args) {
        System.out.println("2024-01-02".compareTo("2024-01-02"));
        System.out.println("2024-01-02".compareTo("2024-01-02 00:00:00"));

        System.out.println("2024-01-02".compareTo("2024-01-03"));
        System.out.println("2024-01-02".compareTo("2024-01-03 00:00:00"));

        System.out.println("2024-01-02 00:00:00".compareTo("2024-01-02"));
        System.out.println("2024-02-02 00:00:00".compareTo("2024-02-03"));
    }
}
