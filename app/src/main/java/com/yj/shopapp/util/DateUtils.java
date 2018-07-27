package com.yj.shopapp.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.yj.shopapp.ui.activity.ShowLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jm on 2016/4/28.
 */
public class DateUtils {
    /**
     * 获取当前的年数
     *
     * @return
     */
    public static int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取当前的月数
     *
     * @return
     */
    public static int getMonths() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前的天数
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据 年、月 获取对应的月份 的 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当天的日期
     *
     * @return yyyy-MM-dd格式的当天的日期
     */
    public static String getNowDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 获取小时
     */
    public static int getHour(long pasttime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(pasttime));
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getDay(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String timet(String time, String pattern) {
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String timed(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    /**
     * 日期字符串转时间戳
     *
     * @param dateStr
     * @param "yyyy-mm-dd"
     * @return
     */
    public static Integer transForMilliSecondByTim(String dateStr, String tim) {
        SimpleDateFormat sdf = new SimpleDateFormat(tim);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) return 0;
        return (int)(date.getTime() / 1000);
    }

    @SuppressLint("DefaultLocale")
    public static String timed(Long time) {
        long hours = (time / (1000 * 60 * 60));
        long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (time % (1000 * 60)) / 1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    public static String timed(Long time, String pattern) {
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        @SuppressWarnings("unused")
        String times = sdr.format(new Date(time));
        return times;
    }

    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String timea(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString2(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分", Locale.CHINA);
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString3(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒", Locale.CHINA);
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString4(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd     hh:mm:ss", Locale.CHINA);
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    /*时间戳转换成字符窜*/
    public static String getDateToLong(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return sf.format(d);

    }

    /*时间戳转换成字符窜*/
    public static String getDateToLong(long time, String pattern) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(d);

    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        if (StringHelper.isEmpty(time)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 对比两个日期大小
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int compareSize(String time1, String time2) {
        Date d1 = strToDate("yyyy-MM-dd", time1);
        Date d2 = strToDate("yyyy-MM-dd", time2);
        return d1.compareTo(d2);
    }

    /**
     * 对比时间大小
     *
     * @param time
     * @return
     */
    public static long ContrastTime(long time) {
        long times = System.currentTimeMillis();
        long currtime = 1000 * time - times;
        if (currtime > 0) {
            return currtime;
        } else {
            return -1;
        }
    }

    public static Long DataToMill(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date d = sdf.parse(time);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @return 相差天数
     * @throws ParseException
     */
    public static int longBetween(String time) {
        //跨年的情况会出现问题哦
        //如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
        Date oDate = strToDate("yyyy-MM-dd", time);
        ShowLog.e(oDate.toString());
        Calendar aCalendar = Calendar.getInstance();
        int day1 = getCurrentDay();
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        int days = day2 - day1;
        ShowLog.e(day1 + "|" + day2);
        return days;
    }

    /**
     * 字符串转成时间类型
     *
     * @return 时间类型
     */
    public static Date strToDate(String style, String mon) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(mon);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 字符串转成时间类型
     *
     * @return 时间类型
     */
    public static boolean ToDate(String style, String mon) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            formatter.parse(mon);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }


    public static String wellcome() {
        String str;
        DateUtils.getNowDate();
        Calendar mCalendar = Calendar.getInstance();
        int apm = mCalendar.get(Calendar.AM_PM);
        if (apm == 0) {
            str = "上午好";
        } else {
            str = "下午好";
        }
        return str;
    }

    /**
     * 获取过去或者未来 任意天内的日期数组
     *
     * @param intervals intervals天内
     * @return 日期数组
     */
    public static ArrayList<String> test(int intervals, String patter) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        ArrayList<String> fetureDaysList = new ArrayList<>();
        for (int i = 0; i < intervals; i++) {
            pastDaysList.add(getPastDate(i, patter));
            fetureDaysList.add(getFetureDate(i));
        }
        return pastDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past, String patter) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patter);
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    public static Long getnowEndTime(int size) {
        Calendar todayEnd = Calendar.getInstance();
        int day = todayEnd.get(Calendar.DATE);
        todayEnd.set(Calendar.DATE, day + size);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis();
    }
}
