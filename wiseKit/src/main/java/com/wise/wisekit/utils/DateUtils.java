package com.wise.wisekit.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String DATE_PATTERN = "yyyy-MM-dd";
    public static String SHORT_DATE_PATTERN = "yy-MM-dd";
    public static String WEEK_PATTERN = "yyyy/MM/dd";
    public static String MONTH_PATTERN = "yyyy-MM";
    public static String TIME_PATTERN = "HH:mm:ss";
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String METHOD_DATE_TIME_PATTERN = "yyyyMMddHHmmss";
    public static String NO_YEAR_DATE_TIME_PATTERN = "MM-dd HH:mm";

    public DateUtils() {
    }

    public static String getMethodDateTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(METHOD_DATE_TIME_PATTERN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getNow() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_PATTERN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(date);
        return dateString;
    }

    public static Date stringToDate(String strDate, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.parse(strDate);
    }

    public static String getNowTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_PATTERN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static boolean isGreaterToday(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        Date d = null;

        try {
            d = sdf.parse(date);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return false;
        }

        return (new Date()).getTime() <= d.getTime();
    }

    public static boolean isGreaterMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(MONTH_PATTERN);
        Date d = null;

        try {
            d = sdf.parse(date);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return false;
        }

        return (new Date()).getTime() <= d.getTime();
    }

    public static boolean isGreaterWeek(String date) {
        String strWeek = date.substring(0, 10);
        SimpleDateFormat sdf = new SimpleDateFormat(WEEK_PATTERN);
        Date d = null;

        try {
            d = sdf.parse(strWeek);
        } catch (ParseException var10) {
            var10.printStackTrace();
            return false;
        }

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(WEEK_PATTERN);
        String dateString = formatter.format(currentTime);

        try {
            Date currentWeek = stringToDate(dateString, WEEK_PATTERN);
            return currentWeek.getTime() <= d.getTime();
        } catch (ParseException var9) {
            var9.printStackTrace();
            return false;
        }
    }

    public static String getBeforeDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(5, -1);
        Date beforeDate = ca.getTime();
        return dateToString(beforeDate, DATE_PATTERN);
    }

    public static String getBeforeDate(String date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(stringToDate(date, DATE_PATTERN));
        ca.add(5, -1);
        Date beforeDate = ca.getTime();
        return dateToString(beforeDate, DATE_PATTERN);
    }

    public static String getBeforeDate(String date, int days) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(stringToDate(date, DATE_PATTERN));
        ca.add(5, -days);
        Date beforeDate = ca.getTime();
        return dateToString(beforeDate, DATE_PATTERN);
    }

    public static String getBeforeDate(Date date, int days) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(5, -days);
        Date beforeDate = ca.getTime();
        return dateToString(beforeDate, DATE_PATTERN);
    }

    public static String getAfterDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(5, 1);
        Date afterDate = ca.getTime();
        return dateToString(afterDate, DATE_PATTERN);
    }

    public static String getAfterDate(String date, int days) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(stringToDate(date, DATE_PATTERN));
        ca.add(5, days);
        Date afterDate = ca.getTime();
        return dateToString(afterDate, DATE_PATTERN);
    }

    public static String getAfterDate(String date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(stringToDate(date, DATE_PATTERN));
        ca.add(5, 1);
        Date afterDate = ca.getTime();
        return dateToString(afterDate, DATE_PATTERN);
    }

    public static String getNowMonth() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(MONTH_PATTERN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getBeforeMonth(String date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(stringToDate(date, MONTH_PATTERN));
        ca.add(2, -1);
        Date beforeDate = ca.getTime();
        return dateToString(beforeDate, MONTH_PATTERN);
    }

    public static String getAfterMonth(String date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(stringToDate(date, MONTH_PATTERN));
        ca.add(2, 1);
        Date afterDate = ca.getTime();
        return dateToString(afterDate, MONTH_PATTERN);
    }

    public static String getMonthBeginDate(String date) throws ParseException {
        return ChangeDatePattern(date, MONTH_PATTERN, DATE_PATTERN);
    }

    public static String getMonthEndDate(String date) throws ParseException {
        String strMonthBeginDate = ChangeDatePattern(date, MONTH_PATTERN, DATE_PATTERN);
        Calendar ca = Calendar.getInstance();
        ca.setTime(stringToDate(date, MONTH_PATTERN));
        ca.add(2, 1);
        ca.add(5, -1);
        Date MonthEndDate = ca.getTime();
        return dateToString(MonthEndDate, DATE_PATTERN);
    }

    public static String getNowWeek() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(WEEK_PATTERN);
        cal.set(7, 1);
        String strNowWeek = df.format(cal.getTime());
        cal.add(5, 6);
        strNowWeek = strNowWeek + "--" + df.format(cal.getTime());
        return strNowWeek;
    }

    public static String getBeforeWeek(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(WEEK_PATTERN);
        cal.setTime(stringToDate(date, WEEK_PATTERN));
        cal.add(5, -7);
        String strBeforeWeek = df.format(cal.getTime());
        cal.add(5, 6);
        strBeforeWeek = strBeforeWeek + "--" + df.format(cal.getTime());
        return strBeforeWeek;
    }

    public static String getAfterWeek(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(WEEK_PATTERN);
        cal.setTime(stringToDate(date, WEEK_PATTERN));
        cal.add(5, 7);
        String strAfterWeek = df.format(cal.getTime());
        cal.add(5, 6);
        strAfterWeek = strAfterWeek + "--" + df.format(cal.getTime());
        return strAfterWeek;
    }

    public static String ChangeDatePattern(String date, String pattern, String toPattern) {
        try {
            return dateToString(stringToDate(date, pattern), toPattern);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String formatTime(String time, String pattern) {
        if(TextUtils.isEmpty(time)) {
            return "1970-01-01";
        } else {
            String newTime = "";

            try {
                Date e = (new SimpleDateFormat(DATE_TIME_PATTERN)).parse(time);
                long timeMillis = e.getTime();
                newTime = (new SimpleDateFormat(pattern)).format(Long.valueOf(timeMillis));
            } catch (ParseException var6) {
                newTime = time;
                var6.printStackTrace();
            }

            return newTime;
        }
    }

    public static String formatTime(String time, String prePattern, String pattern) {
        if(TextUtils.isEmpty(time)) {
            return "1970-01-01";
        } else {
            String newTime = "";

            try {
                Date e = (new SimpleDateFormat(prePattern)).parse(time);
                long timeMillis = e.getTime();
                newTime = (new SimpleDateFormat(pattern)).format(Long.valueOf(timeMillis));
            } catch (ParseException var7) {
                newTime = time;
                var7.printStackTrace();
            }

            return newTime;
        }
    }

    public static int compareTime(String time1, String time2) {
        boolean status = true;

        byte status1;
        try {
            Date date1 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time1);
            Date e = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time2);
            long timeMillis1 = date1.getTime();
            long timeMillis2 = e.getTime();
            if(timeMillis1 == timeMillis2) {
                status1 = 0;
            } else if(timeMillis1 > timeMillis2) {
                status1 = 1;
            } else {
                status1 = -1;
            }
        } catch (ParseException var9) {
            var9.printStackTrace();
            status1 = -1;
        }

        return status1;
    }

    public static long getTimeMillis(String time, String pattern) {
        Date date = null;

        try {
            date = (new SimpleDateFormat(pattern)).parse(time);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return 0L;
        }

        return date.getTime();
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(1);
        return currentYear;
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(2) + 1;
        return currentMonth;
    }
}
