package org.programlife.investment.stock.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static long parseMills(String date) {
        if (date.length() == 10){
            date = date + " 00:00:00";
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

    public static String parseDateStr(long timeMills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMills);
        return simpleDateFormat.format(date);
    }

    public static String parseDateStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
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

    public static void main(String[] args) {
        System.out.println("2024-01-02".compareTo("2024-01-02"));
        System.out.println("2024-01-02".compareTo("2024-01-02 00:00:00"));

        System.out.println("2024-01-02".compareTo("2024-01-03"));
        System.out.println("2024-01-02".compareTo("2024-01-03 00:00:00"));

        System.out.println("2024-01-02 00:00:00".compareTo("2024-01-02"));
        System.out.println("2024-02-02 00:00:00".compareTo("2024-02-03"));

    }
}
